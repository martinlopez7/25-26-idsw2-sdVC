package com.martin.exam_generator.service;

import com.martin.exam_generator.dto.generacion.PlantillaExamen;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ExamenSessionService {

    private static final String PLANTILLAS_EXAMENES_KEY = "plantillasExamenes";

    private final HttpSession httpSession;

    public ExamenSessionService(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    @SuppressWarnings("unchecked")
    public List<PlantillaExamen> obtenerPlantillas() {
        System.out.println("Session ID: " + httpSession.getId());
        System.out.println("Session isNew: " + httpSession.isNew());
        List<PlantillaExamen> plantillas = (List<PlantillaExamen>) httpSession.getAttribute(PLANTILLAS_EXAMENES_KEY);
        System.out.println("Plantillas encontradas: " + (plantillas != null ? plantillas.size() : 0));
        return plantillas != null ? new ArrayList<>(plantillas) : new ArrayList<>();
    }

    public Optional<PlantillaExamen> obtenerPlantillaPorId(String id) {
        return obtenerPlantillas().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    public void almacenarPlantillas(List<PlantillaExamen> plantillas) {
        httpSession.setAttribute(PLANTILLAS_EXAMENES_KEY, new ArrayList<>(plantillas));
    }

    public void actualizarPlantilla(PlantillaExamen plantillaActualizada) {
        List<PlantillaExamen> plantillas = obtenerPlantillas();
        for (int i = 0; i < plantillas.size(); i++) {
            if (plantillas.get(i).getId().equals(plantillaActualizada.getId())) {
                plantillas.set(i, plantillaActualizada);
                break;
            }
        }
        httpSession.setAttribute(PLANTILLAS_EXAMENES_KEY, plantillas);
    }

    public void limpiarPlantillas() {
        httpSession.removeAttribute(PLANTILLAS_EXAMENES_KEY);
    }

    public List<PlantillaExamen> obtenerExamenesAsignados() {
        return obtenerPlantillas().stream()
                .filter(p -> p.getAsignada() != null && p.getAsignada())
                .toList();
    }

    public void marcarComoAsignada(String plantillaId, Long alumnoId, String claveCorreccion) {
        obtenerPlantillaPorId(plantillaId).ifPresent(plantilla -> {
            plantilla.setAlumnoId(alumnoId);
            plantilla.setClaveCorreccion(claveCorreccion);
            plantilla.setClavePendiente(false);
            plantilla.setAsignada(true);
            actualizarPlantilla(plantilla);
        });
    }

    public boolean existeAsignacionParaAlumno(Long alumnoId) {
        return obtenerPlantillas().stream()
                .anyMatch(p -> p.getAlumnoId() != null && p.getAlumnoId().equals(alumnoId));
    }

}