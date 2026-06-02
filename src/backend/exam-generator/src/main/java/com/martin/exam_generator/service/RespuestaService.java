package com.martin.exam_generator.service;

import com.martin.exam_generator.dto.RespuestaDTO;
import com.martin.exam_generator.dto.RespuestaUpdateDTO;
import com.martin.exam_generator.entities.Pregunta;
import com.martin.exam_generator.entities.Respuesta;
import com.martin.exam_generator.repository.PreguntaRepository;
import com.martin.exam_generator.repository.RespuestaRepository;
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
    private final PreguntaRepository preguntaRepository;

    public RespuestaService(RespuestaRepository respuestaRepository, PreguntaRepository preguntaRepository) {
        this.respuestaRepository = respuestaRepository;
        this.preguntaRepository = preguntaRepository;
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
        Pregunta pregunta = preguntaRepository.findById(preguntaId)
                .orElseThrow(() -> new EntityNotFoundException("Pregunta no encontrada con id: " + preguntaId));

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
    public void eliminarRespuesta(Long id) {
        Respuesta respuesta = respuestaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Respuesta no encontrada con id: " + id));
        respuestaRepository.delete(respuesta);
    }
}
