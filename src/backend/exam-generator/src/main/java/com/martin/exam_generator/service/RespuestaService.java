package com.martin.exam_generator.service;

import com.martin.exam_generator.dto.PreguntaDTO;
import com.martin.exam_generator.dto.RespuestaCreateDTO;
import com.martin.exam_generator.dto.RespuestaDTO;
import com.martin.exam_generator.dto.RespuestaUpdateDTO;
import com.martin.exam_generator.entities.Pregunta;
import com.martin.exam_generator.entities.Respuesta;
import com.martin.exam_generator.repository.RespuestaRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class RespuestaService {

    private final RespuestaRepository respuestaRepository;
    private final PreguntaService preguntaService;

    public RespuestaService(RespuestaRepository respuestaRepository, @Lazy PreguntaService preguntaService) {
        this.respuestaRepository = respuestaRepository;
        this.preguntaService = preguntaService;
    }

    @Transactional
    public RespuestaDTO crearRespuesta(RespuestaCreateDTO dto, Long docenteId) {
        PreguntaDTO preguntaDTO = preguntaService.obtenerPreguntaPorId(dto.getPreguntaId());
        Pregunta pregunta = new Pregunta();
        pregunta.setId(preguntaDTO.getId());

        if (!preguntaService.verificarPreguntaPerteneceADocente(dto.getPreguntaId(), docenteId)) {
            throw new EntityNotFoundException("Pregunta no encontrada con id: " + dto.getPreguntaId());
        }

        Respuesta respuesta = new Respuesta();
        respuesta.setOpcion(dto.getOpcion());
        respuesta.setEsCorrecta(dto.getEsCorrecta());
        respuesta.setPregunta(pregunta);

        Respuesta guardada = respuestaRepository.save(respuesta);
        return RespuestaDTO.fromEntity(guardada);
    }

    public List<RespuestaDTO> obtenerRespuestasPorPregunta(Long preguntaId) {
        return respuestaRepository.findByPreguntaId(preguntaId).stream()
                .map(RespuestaDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<RespuestaDTO> filtrarRespuestasPorPregunta(Long preguntaId, String criterio) {
        return respuestaRepository.findByPreguntaIdAndOpcionContainingIgnoreCase(preguntaId, criterio).stream()
                .map(RespuestaDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<Respuesta> procesarRespuestas(Long preguntaId, List<RespuestaUpdateDTO> respuestasUpdate) {
        Pregunta pregunta = preguntaService.obtenerPreguntaEntityPorId(preguntaId);

        List<Respuesta> respuestasActuales = new ArrayList<>(pregunta.getRespuestas());

        for (RespuestaUpdateDTO respUpdate : respuestasUpdate) {
            if (respUpdate.getId() != null) {
                Respuesta existente = respuestasActuales.stream()
                        .filter(r -> r.getId().equals(respUpdate.getId()))
                        .findFirst()
                        .orElseThrow(() -> new EntityNotFoundException("Respuesta no encontrada con id: " + respUpdate.getId()));

                existente.setOpcion(respUpdate.getOpcion());
                existente.setEsCorrecta(respUpdate.getEsCorrecta());
                respuestasActuales.remove(existente);
            } else {
                Respuesta nueva = new Respuesta();
                nueva.setOpcion(respUpdate.getOpcion());
                nueva.setEsCorrecta(respUpdate.getEsCorrecta());
                nueva.setPregunta(pregunta);
                respuestaRepository.save(nueva);
            }
        }

        for (Respuesta r : respuestasActuales) {
            respuestaRepository.delete(r);
        }

        return respuestaRepository.findByPreguntaId(preguntaId);
    }

    @Transactional
    public void eliminarRespuesta(Long id, Long docenteId) {
        Respuesta respuesta = respuestaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Respuesta no encontrada con id: " + id));

        Long preguntaId = respuesta.getPregunta().getId();
        if (!preguntaService.verificarPreguntaPerteneceADocente(preguntaId, docenteId)) {
            throw new EntityNotFoundException("Respuesta no encontrada con id: " + id);
        }

        respuestaRepository.delete(respuesta);
    }

    public RespuestaDTO obtenerRespuestaPorId(Long id, Long docenteId) {
        Respuesta respuesta = respuestaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Respuesta no encontrada con id: " + id));

        Long preguntaId = respuesta.getPregunta().getId();
        if (!preguntaService.verificarPreguntaPerteneceADocente(preguntaId, docenteId)) {
            throw new EntityNotFoundException("Respuesta no encontrada con id: " + id);
        }

        return RespuestaDTO.fromEntity(respuesta);
    }

    @Transactional
    public RespuestaDTO actualizarRespuesta(Long id, RespuestaUpdateDTO dto, Long docenteId) {
        Respuesta respuesta = respuestaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Respuesta no encontrada con id: " + id));

        Long preguntaId = respuesta.getPregunta().getId();
        if (!preguntaService.verificarPreguntaPerteneceADocente(preguntaId, docenteId)) {
            throw new EntityNotFoundException("Respuesta no encontrada con id: " + id);
        }

        respuesta.setOpcion(dto.getOpcion());
        respuesta.setEsCorrecta(dto.getEsCorrecta());

        Respuesta guardada = respuestaRepository.save(respuesta);
        return RespuestaDTO.fromEntity(guardada);
    }
}
