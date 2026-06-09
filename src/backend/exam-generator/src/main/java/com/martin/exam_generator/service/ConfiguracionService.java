package com.martin.exam_generator.service;

import com.martin.exam_generator.dto.config.ConfiguracionExportDTO;
import com.martin.exam_generator.dto.config.ConfiguracionExportDTO.*;
import com.martin.exam_generator.entities.*;
import com.martin.exam_generator.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ConfiguracionService {

    private final GradoRepository gradoRepository;
    private final AsignaturaRepository asignaturaRepository;
    private final AlumnoRepository alumnoRepository;
    private final PreguntaRepository preguntaRepository;
    private final RespuestaRepository respuestaRepository;
    private final UsuarioRepository usuarioRepository;

    public ConfiguracionService(
            GradoRepository gradoRepository,
            AsignaturaRepository asignaturaRepository,
            AlumnoRepository alumnoRepository,
            PreguntaRepository preguntaRepository,
            RespuestaRepository respuestaRepository,
            UsuarioRepository usuarioRepository) {
        this.gradoRepository = gradoRepository;
        this.asignaturaRepository = asignaturaRepository;
        this.alumnoRepository = alumnoRepository;
        this.preguntaRepository = preguntaRepository;
        this.respuestaRepository = respuestaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public ConfiguracionExportDTO exportarConfiguracionGlobal(Long docenteId) {
        Usuario docente = usuarioRepository.findById(docenteId)
                .orElseThrow(() -> new RuntimeException("Docente no encontrado"));

        List<GradoExportDTO> grados = gradoRepository.findByDocenteId(docenteId).stream()
                .map(g -> new GradoExportDTO(g.getId(), g.getTitulo(), g.getCodigo()))
                .collect(Collectors.toList());

        List<AsignaturaExportDTO> asignaturas = asignaturaRepository.findByDocenteId(docenteId).stream()
                .map(a -> new AsignaturaExportDTO(
                        a.getId(),
                        a.getTitulo(),
                        a.getCodigo(),
                        a.getCursoAcademico(),
                        a.getGrados() != null ?
                                a.getGrados().stream().map(g -> g.getId()).collect(Collectors.toList()) :
                                java.util.Collections.emptyList()
                ))
                .collect(Collectors.toList());

        List<AlumnoExportDTO> alumnos = alumnoRepository.findByDocenteId(docenteId).stream()
                .map(a -> new AlumnoExportDTO(
                        a.getId(),
                        a.getNombre(),
                        a.getApellidos(),
                        a.getDni(),
                        a.getGrado() != null ? a.getGrado().getId() : null,
                        a.getAsignaturas() != null ?
                                a.getAsignaturas().stream().map(as -> as.getId()).collect(Collectors.toList()) :
                                java.util.Collections.emptyList()
                ))
                .collect(Collectors.toList());

        List<PreguntaExportDTO> preguntas = preguntaRepository.findByDocenteId(docenteId).stream()
                .map(this::mapPreguntaToExport)
                .collect(Collectors.toList());

        ElementosExportDTO elementos = new ElementosExportDTO(grados, asignaturas, alumnos, preguntas);

        return new ConfiguracionExportDTO(
                "1.0",
                LocalDateTime.now(),
                new DocenteOrigenDTO(docente.getId(), docente.getNombre(), docente.getApellidos()),
                elementos
        );
    }

    private PreguntaExportDTO mapPreguntaToExport(Pregunta pregunta) {
        List<RespuestaExportDTO> respuestas = pregunta.getRespuestas().stream()
                .map(r -> new RespuestaExportDTO(r.getOpcion(), r.getEsCorrecta()))
                .collect(Collectors.toList());

        return new PreguntaExportDTO(
                pregunta.getId(),
                pregunta.getEnunciado(),
                pregunta.getTema(),
                pregunta.getDificultad().name(),
                pregunta.getAsignaturaId(),
                respuestas
        );
    }

    @Transactional
    public void importarConfiguracionGlobal(ConfiguracionExportDTO configuracion, Long docenteId) {
        eliminarDatosDocente(docenteId);

        Map<Long, Long> gradoIdMap = new HashMap<>();
        List<Grado> gradosImportados = importarGrados(configuracion.getElementos().getGrados(), docenteId, gradoIdMap);

        Map<Long, Long> asignaturaIdMap = new HashMap<>();
        List<Asignatura> asignaturasImportadas = importarAsignaturas(
                configuracion.getElementos().getAsignaturas(),
                docenteId,
                gradoIdMap,
                asignaturaIdMap
        );

        importarAlumnos(
                configuracion.getElementos().getAlumnos(),
                docenteId,
                gradoIdMap,
                asignaturaIdMap
        );

        importarPreguntas(
                configuracion.getElementos().getPreguntas(),
                docenteId,
                asignaturaIdMap
        );
    }

    private void eliminarDatosDocente(Long docenteId) {
        preguntaRepository.deleteByDocenteId(docenteId);
        asignaturaRepository.deleteByDocenteId(docenteId);
        alumnoRepository.deleteByDocenteId(docenteId);
        gradoRepository.deleteByDocenteId(docenteId);
    }

    private List<Grado> importarGrados(List<GradoExportDTO> gradosDTO, Long docenteId, Map<Long, Long> gradoIdMap) {
        List<Grado> grados = new ArrayList<>();
        for (GradoExportDTO gDTO : gradosDTO) {
            Grado grado = new Grado();
            grado.setTitulo(gDTO.getTitulo());
            grado.setCodigo(gDTO.getCodigo());
            grado.setDocenteId(docenteId);
            Grado savedGrado = gradoRepository.save(grado);
            gradoIdMap.put(gDTO.getId(), savedGrado.getId());
            grados.add(savedGrado);
        }
        return grados;
    }

    private List<Asignatura> importarAsignaturas(
            List<AsignaturaExportDTO> asignaturasDTO,
            Long docenteId,
            Map<Long, Long> gradoIdMap,
            Map<Long, Long> asignaturaIdMap) {
        List<Asignatura> asignaturas = new ArrayList<>();
        for (AsignaturaExportDTO aDTO : asignaturasDTO) {
            Asignatura asignatura = new Asignatura();
            asignatura.setTitulo(aDTO.getTitulo());
            asignatura.setCodigo(aDTO.getCodigo());
            asignatura.setCursoAcademico(aDTO.getCursoAcademico());
            asignatura.setDocenteId(docenteId);

            if (aDTO.getGradoIds() != null && !aDTO.getGradoIds().isEmpty()) {
                List<Grado> gradosAsociados = new ArrayList<>();
                for (Long gradoIdOriginal : aDTO.getGradoIds()) {
                    Long gradoIdNuevo = gradoIdMap.get(gradoIdOriginal);
                    if (gradoIdNuevo != null) {
                        Grado grado = gradoRepository.findById(gradoIdNuevo).orElse(null);
                        if (grado != null) {
                            gradosAsociados.add(grado);
                        }
                    }
                }
                asignatura.setGrados(gradosAsociados);
            }

            Asignatura savedAsignatura = asignaturaRepository.save(asignatura);
            asignaturaIdMap.put(aDTO.getId(), savedAsignatura.getId());
            asignaturas.add(savedAsignatura);
        }
        return asignaturas;
    }

    private void importarAlumnos(
            List<AlumnoExportDTO> alumnosDTO,
            Long docenteId,
            Map<Long, Long> gradoIdMap,
            Map<Long, Long> asignaturaIdMap) {
        for (AlumnoExportDTO aDTO : alumnosDTO) {
            Alumno alumno = new Alumno();
            alumno.setNombre(aDTO.getNombre());
            alumno.setApellidos(aDTO.getApellidos());
            alumno.setDni(aDTO.getDni());
            alumno.setDocenteId(docenteId);

            if (aDTO.getGradoId() != null) {
                Long gradoIdNuevo = gradoIdMap.get(aDTO.getGradoId());
                if (gradoIdNuevo != null) {
                    alumno.setGrado(gradoRepository.findById(gradoIdNuevo).orElse(null));
                }
            }

            if (aDTO.getAsignaturaIds() != null && !aDTO.getAsignaturaIds().isEmpty()) {
                List<Asignatura> asignaturasAsociadas = new ArrayList<>();
                for (Long asignaturaIdOriginal : aDTO.getAsignaturaIds()) {
                    Long asignaturaIdNuevo = asignaturaIdMap.get(asignaturaIdOriginal);
                    if (asignaturaIdNuevo != null) {
                        Asignatura asignatura = asignaturaRepository.findById(asignaturaIdNuevo).orElse(null);
                        if (asignatura != null) {
                            asignaturasAsociadas.add(asignatura);
                        }
                    }
                }
                alumno.setAsignaturas(asignaturasAsociadas);
            }

            alumnoRepository.save(alumno);
        }
    }

    private void importarPreguntas(
            List<PreguntaExportDTO> preguntasDTO,
            Long docenteId,
            Map<Long, Long> asignaturaIdMap) {
        for (PreguntaExportDTO pDTO : preguntasDTO) {
            Pregunta pregunta = new Pregunta();
            pregunta.setEnunciado(pDTO.getEnunciado());
            pregunta.setTema(pDTO.getTema());
            pregunta.setDificultad(Pregunta.Dificultad.valueOf(pDTO.getDificultad()));
            pregunta.setDocenteId(docenteId);

            if (pDTO.getAsignaturaId() != null) {
                Long asignaturaIdNuevo = asignaturaIdMap.get(pDTO.getAsignaturaId());
                if (asignaturaIdNuevo != null) {
                    pregunta.setAsignaturaId(asignaturaIdNuevo);
                }
            }

            List<Respuesta> respuestas = new ArrayList<>();
            if (pDTO.getRespuestas() != null) {
                for (RespuestaExportDTO rDTO : pDTO.getRespuestas()) {
                    Respuesta respuesta = new Respuesta();
                    respuesta.setOpcion(rDTO.getOpcion());
                    respuesta.setEsCorrecta(rDTO.getEsCorrecta());
                    respuesta.setPregunta(pregunta);
                    respuestas.add(respuesta);
                }
            }
            pregunta.setRespuestas(respuestas);

            preguntaRepository.save(pregunta);
        }
    }
}