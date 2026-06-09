package com.martin.exam_generator.service;

import com.martin.exam_generator.dto.alumno.AlumnoDTO;
import com.martin.exam_generator.dto.asignacion.AsignacionDTO;
import com.martin.exam_generator.dto.asignatura.AsignaturaConGradosDTO;
import com.martin.exam_generator.dto.generacion.*;
import com.martin.exam_generator.dto.grado.GradoConAlumnosDTO;
import com.martin.exam_generator.dto.grado.GradoDTO;
import com.martin.exam_generator.dto.pregunta.PreguntaDTO;
import com.martin.exam_generator.entities.Pregunta;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExamenService {

    private final AsignaturaService asignaturaService;
    private final PreguntaService preguntaService;
    private final ExamenSessionService examenSessionService;
    private final AlumnoService alumnoService;
    private final PdfGenerationService pdfGenerationService;

    public ExamenService(AsignaturaService asignaturaService,
                         PreguntaService preguntaService,
                         ExamenSessionService examenSessionService,
                         AlumnoService alumnoService,
                         PdfGenerationService pdfGenerationService) {
        this.asignaturaService = asignaturaService;
        this.preguntaService = preguntaService;
        this.examenSessionService = examenSessionService;
        this.alumnoService = alumnoService;
        this.pdfGenerationService = pdfGenerationService;
    }

    public GenerarExamenesResponse generarExamenes(GenerarExamenesRequest request, Long docenteId) {
        AsignaturaConGradosDTO asignatura = asignaturaService.obtenerAsignaturaConGradosYAlumnos(
                request.getAsignaturaId(), docenteId);

        validarDatos(request, asignatura);

        List<PreguntaDTO> preguntas = preguntaService.obtenerPreguntasPorAsignaturaYTemas(
                request.getAsignaturaId(), request.getTemas());

        if (preguntas.size() < request.getNumPreguntas()) {
            throw new IllegalStateException("No hay suficientes preguntas en la batería para los criterios seleccionados");
        }

        Map<String, List<PreguntaDTO>> preguntasPorDificultad = preguntaService.clasificarPreguntasPorDificultad(preguntas);

        List<PlantillaExamen> plantillas = new ArrayList<>();

        for (Map.Entry<Long, ConfigGradoDTO> entry : request.getConfigPorGrado().entrySet()) {
            Long gradoId = entry.getKey();
            ConfigGradoDTO config = entry.getValue();

            validarLimitesPorGrado(config, gradoId, asignatura, request.getNumPreguntas());

            for (int i = 0; i < config.getNumExamenes(); i++) {
                List<PlantillaExamen.PreguntaSeleccionada> preguntasSeleccionadas = seleccionarPreguntas(
                        preguntasPorDificultad,
                        request.getNumPreguntas(),
                        config.getProporcionDificultad()
                );

                aleatorizarPreguntasYRespuestas(preguntasSeleccionadas);

                PlantillaExamen plantilla = PlantillaExamen.crear(
                        asignatura.getId(),
                        asignatura.getTitulo(),
                        request.getEvaluacion(),
                        preguntasSeleccionadas,
                        gradoId
                );

                plantillas.add(plantilla);
            }
        }

        examenSessionService.almacenarPlantillas(plantillas);

        List<String> ids = plantillas.stream().map(PlantillaExamen::getId).collect(Collectors.toList());
        return new GenerarExamenesResponse(ids, plantillas.size());
    }

    public void cancelarGeneracion() {
        examenSessionService.limpiarPlantillas();
    }

    private void validarDatos(GenerarExamenesRequest request, AsignaturaConGradosDTO asignatura) {
        if (asignatura.getGrados() == null || asignatura.getGrados().isEmpty()) {
            throw new IllegalArgumentException("La asignatura no tiene grados asociados");
        }

        for (Long gradoId : request.getConfigPorGrado().keySet()) {
            boolean gradoExiste = asignatura.getGrados().stream()
                    .anyMatch(g -> g.getId().equals(gradoId));
            if (!gradoExiste) {
                throw new IllegalArgumentException("El grado con id " + gradoId + " no está asociado a la asignatura");
            }
        }

        for (ConfigGradoDTO config : request.getConfigPorGrado().values()) {
            int totalProporcion = 0;
            if (config.getProporcionDificultad().getFacil() != null) {
                totalProporcion += config.getProporcionDificultad().getFacil();
            }
            if (config.getProporcionDificultad().getMedio() != null) {
                totalProporcion += config.getProporcionDificultad().getMedio();
            }
            if (config.getProporcionDificultad().getDificil() != null) {
                totalProporcion += config.getProporcionDificultad().getDificil();
            }
            if (totalProporcion != 100) {
                throw new IllegalArgumentException("La proporción de dificultad debe sumar 100");
            }
        }
    }

    private void validarLimitesPorGrado(ConfigGradoDTO config, Long gradoId, AsignaturaConGradosDTO asignatura, int numPreguntasTotal) {
        int numAlumnos = asignatura.getGrados().stream()
                .filter(g -> g.getId().equals(gradoId))
                .findFirst()
                .map(g -> g.getNumAlumnos())
                .orElse(0);

        if (config.getNumExamenes() > numAlumnos) {
            throw new IllegalArgumentException("El número de exámenes (" + config.getNumExamenes() +
                    ") no puede superar el número de alumnos matriculados (" + numAlumnos + ")");
        }

        if (config.getNumTiposExamen() > numAlumnos) {
            throw new IllegalArgumentException("El número de tipos de examen (" + config.getNumTiposExamen() +
                    ") no puede superar el número de alumnos matriculados (" + numAlumnos + ")");
        }
    }

    private List<PlantillaExamen.PreguntaSeleccionada> seleccionarPreguntas(
            Map<String, List<PreguntaDTO>> preguntasPorDificultad,
            int numPreguntas,
            ConfigGradoDTO.ProporcionDificultadDTO proporcion) {

        List<PlantillaExamen.PreguntaSeleccionada> seleccionadas = new ArrayList<>();

        int numFacil = (int) Math.ceil(numPreguntas * proporcion.getFacil() / 100.0);
        int numMedio = (int) Math.ceil(numPreguntas * proporcion.getMedio() / 100.0);
        int numDificil = numPreguntas - numFacil - numMedio;

        seleccionarPreguntasPorDificultad(preguntasPorDificultad, "FACIL", numFacil, seleccionadas);
        seleccionarPreguntasPorDificultad(preguntasPorDificultad, "MEDIO", numMedio, seleccionadas);
        seleccionarPreguntasPorDificultad(preguntasPorDificultad, "DIFICIL", numDificil, seleccionadas);

        return seleccionadas;
    }

    private void seleccionarPreguntasPorDificultad(
            Map<String, List<PreguntaDTO>> preguntasPorDificultad,
            String dificultad,
            int cantidad,
            List<PlantillaExamen.PreguntaSeleccionada> seleccionadas) {

        List<PreguntaDTO> disponibles = preguntasPorDificultad.getOrDefault(dificultad, new ArrayList<>());
        Collections.shuffle(disponibles);

        for (int i = 0; i < cantidad && i < disponibles.size(); i++) {
            PreguntaDTO preguntaDTO = disponibles.get(i);
            PlantillaExamen.PreguntaSeleccionada ps = new PlantillaExamen.PreguntaSeleccionada();
            ps.setPreguntaId(preguntaDTO.getId());
            ps.setEnunciado(preguntaDTO.getEnunciado());
            ps.setTema(preguntaDTO.getTema());
            ps.setDificultad(Pregunta.Dificultad.valueOf(preguntaDTO.getDificultad()));
            ps.setPosicionOriginal(i);

            List<PlantillaExamen.RespuestaSeleccionada> respuestas = new ArrayList<>();
            if (preguntaDTO.getRespuestas() != null) {
                int pos = 0;
                for (var respuestaDTO : preguntaDTO.getRespuestas()) {
                    PlantillaExamen.RespuestaSeleccionada rs = new PlantillaExamen.RespuestaSeleccionada();
                    rs.setRespuestaId(respuestaDTO.getId());
                    rs.setOpcion(respuestaDTO.getOpcion());
                    rs.setEsCorrecta(respuestaDTO.getEsCorrecta());
                    rs.setPosicionOriginal(pos++);
                    respuestas.add(rs);
                }
            }
            ps.setRespuestas(respuestas);
            seleccionadas.add(ps);
        }
    }

    private void aleatorizarPreguntasYRespuestas(List<PlantillaExamen.PreguntaSeleccionada> preguntas) {
        Collections.shuffle(preguntas);
        for (int i = 0; i < preguntas.size(); i++) {
            preguntas.get(i).setPosicionAleatoria(i);
            if (preguntas.get(i).getRespuestas() != null) {
                Collections.shuffle(preguntas.get(i).getRespuestas());
                for (int j = 0; j < preguntas.get(i).getRespuestas().size(); j++) {
                    preguntas.get(i).getRespuestas().get(j).setPosicionAleatoria(j);
                }
            }
        }
    }

    public List<PlantillaExamenDTO> obtenerPlantillas() {
        List<PlantillaExamen> plantillas = examenSessionService.obtenerPlantillas();
        return plantillas.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Map<GradoDTO, List<AlumnoDTO>> obtenerAlumnosPorGrado(Long asignaturaId, Long docenteId) {
        AsignaturaConGradosDTO asignatura = asignaturaService.obtenerAsignaturaConGradosYAlumnos(asignaturaId, docenteId);

        if (asignatura.getGrados() == null || asignatura.getGrados().isEmpty()) {
            return Collections.emptyMap();
        }

        List<Long> gradoIds = asignatura.getGrados().stream()
                .map(GradoConAlumnosDTO::getId)
                .collect(Collectors.toList());

        List<AlumnoDTO> allAlumnos = alumnoService.obtenerAlumnosPorAsignaturaYGrados(asignaturaId, gradoIds);

        Map<GradoDTO, List<AlumnoDTO>> result = new LinkedHashMap<>();
        for (GradoConAlumnosDTO gradoDTO : asignatura.getGrados()) {
            GradoDTO grado = new GradoDTO(gradoDTO.getId(), gradoDTO.getTitulo(), gradoDTO.getCodigo());
            List<AlumnoDTO> alumnosDelGrado = allAlumnos.stream()
                    .filter(a -> a.getGradoId() != null && a.getGradoId().equals(gradoDTO.getId()))
                    .collect(Collectors.toList());
            result.put(grado, alumnosDelGrado);
        }

        return result;
    }

    public void asignarExamen(String plantillaId, Long alumnoId) {
        if (examenSessionService.existeAsignacionParaAlumno(alumnoId)) {
            throw new IllegalStateException("Este alumno ya tiene un examen asignado");
        }

        Optional<PlantillaExamen> optPlantilla = examenSessionService.obtenerPlantillaPorId(plantillaId);
        if (optPlantilla.isEmpty()) {
            throw new IllegalArgumentException("No se encontró la plantilla de examen");
        }

        PlantillaExamen plantilla = optPlantilla.get();

        if (!alumnoService.verificarAlumnoPerteneceAGrado(alumnoId, plantilla.getGradoId())) {
            throw new IllegalArgumentException("El alumno no pertenece al grado correspondiente a este examen");
        }

        String claveCorreccion = generarClaveCorreccion(plantilla, alumnoId);

        examenSessionService.marcarComoAsignada(plantillaId, alumnoId, claveCorreccion);
    }

    public int asignarTodos(List<AsignacionDTO> asignaciones) {
        int asignadas = 0;
        List<Long> alumnosYaAsignados = new ArrayList<>();

        for (AsignacionDTO asignacion : asignaciones) {
            if (alumnosYaAsignados.contains(asignacion.getAlumnoId())) {
                throw new IllegalStateException("El alumno " + asignacion.getAlumnoId() + " aparece múltiples veces en las asignaciones");
            }

            if (examenSessionService.existeAsignacionParaAlumno(asignacion.getAlumnoId())) {
                throw new IllegalStateException("El alumno " + asignacion.getAlumnoId() + " ya tiene un examen asignado");
            }

            Optional<PlantillaExamen> optPlantilla = examenSessionService.obtenerPlantillaPorId(asignacion.getPlantillaId());
            if (optPlantilla.isEmpty()) {
                throw new IllegalArgumentException("No se encontró la plantilla: " + asignacion.getPlantillaId());
            }

            PlantillaExamen plantilla = optPlantilla.get();

            if (!alumnoService.verificarAlumnoPerteneceAGrado(asignacion.getAlumnoId(), plantilla.getGradoId())) {
                throw new IllegalArgumentException("El alumno " + asignacion.getAlumnoId() + " no pertenece al grado correspondiente a este examen");
            }

            String claveCorreccion = generarClaveCorreccion(plantilla, asignacion.getAlumnoId());
            examenSessionService.marcarComoAsignada(asignacion.getPlantillaId(), asignacion.getAlumnoId(), claveCorreccion);

            alumnosYaAsignados.add(asignacion.getAlumnoId());
            asignadas++;
        }

        return asignadas;
    }

    public byte[] generarZipExamenes() {
        List<PlantillaExamen> plantillas = examenSessionService.obtenerExamenesAsignados();

        if (plantillas.isEmpty()) {
            throw new IllegalStateException("No hay exámenes asignados para descargar");
        }

        List<Long> alumnoIds = plantillas.stream()
                .filter(p -> p.getAlumnoId() != null)
                .map(PlantillaExamen::getAlumnoId)
                .collect(Collectors.toList());

        Map<Long, String> alumnoInfoMap = new HashMap<>();
        for (Long alumnoId : alumnoIds) {
            AlumnoDTO alumno = new AlumnoDTO();
            alumno.setId(alumnoId);
            alumno.setNombre("Alumno");
            alumno.setApellidos("Apellidos");
            alumnoInfoMap.put(alumnoId, "Alumno Apellidos");
        }

        return pdfGenerationService.generarZipExamenes(plantillas, alumnoInfoMap);
    }

    private String generarClaveCorreccion(PlantillaExamen plantilla, Long alumnoId) {
        StringBuilder sb = new StringBuilder();
        sb.append("P:");
        if (plantilla.getPreguntas() != null) {
            for (PlantillaExamen.PreguntaSeleccionada pregunta : plantilla.getPreguntas()) {
                sb.append(pregunta.getPreguntaId()).append("|");
                if (pregunta.getRespuestas() != null) {
                    for (PlantillaExamen.RespuestaSeleccionada respuesta : pregunta.getRespuestas()) {
                        if (respuesta.getEsCorrecta() != null && respuesta.getEsCorrecta()) {
                            sb.append(respuesta.getRespuestaId()).append(",");
                        }
                    }
                }
                sb.append(";");
            }
        }
        sb.append("A:").append(alumnoId);

        return hashMD5(sb.toString());
    }

    private String hashMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al generar hash MD5", e);
        }
    }

    private PlantillaExamenDTO toDTO(PlantillaExamen plantilla) {
        PlantillaExamenDTO dto = new PlantillaExamenDTO();
        dto.setId(plantilla.getId());
        dto.setAsignaturaId(plantilla.getAsignaturaId());
        dto.setTituloAsignatura(plantilla.getTituloAsignatura());
        dto.setEvaluacion(plantilla.getEvaluacion() != null ? plantilla.getEvaluacion().name() : null);
        dto.setNumPreguntas(plantilla.getPreguntas() != null ? plantilla.getPreguntas().size() : 0);
        dto.setAlumnoId(plantilla.getAlumnoId());
        dto.setClaveCorreccion(plantilla.getClaveCorreccion());
        dto.setAsignada(plantilla.getAsignada());
        dto.setGradoId(plantilla.getGradoId());
        return dto;
    }
}