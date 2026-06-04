package com.martin.exam_generator.service;

import com.martin.exam_generator.dto.AsignaturaConGradosDTO;
import com.martin.exam_generator.dto.ConfigGradoDTO;
import com.martin.exam_generator.dto.GenerarExamenesRequest;
import com.martin.exam_generator.dto.GenerarExamenesResponse;
import com.martin.exam_generator.dto.PlantillaExamen;
import com.martin.exam_generator.dto.PreguntaDTO;
import com.martin.exam_generator.entities.Pregunta;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExamenService {

    private final AsignaturaService asignaturaService;
    private final PreguntaService preguntaService;
    private final ExamenSessionService examenSessionService;

    public ExamenService(AsignaturaService asignaturaService,
                         PreguntaService preguntaService,
                         ExamenSessionService examenSessionService) {
        this.asignaturaService = asignaturaService;
        this.preguntaService = preguntaService;
        this.examenSessionService = examenSessionService;
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
                        preguntasSeleccionadas
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
}