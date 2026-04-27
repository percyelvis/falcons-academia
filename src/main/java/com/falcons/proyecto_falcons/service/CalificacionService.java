package com.falcons.proyecto_falcons.service;

import com.falcons.proyecto_falcons.entity.Calificacion;
import com.falcons.proyecto_falcons.repository.CalificacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CalificacionService {

    @Autowired
    private CalificacionRepository calificacionRepository;

    // =========================
    // ➕ CREAR
    // =========================
    public Calificacion crearCalificacion(Calificacion calificacion) {
        return calificacionRepository.save(calificacion);
    }

    // =========================
    // 📋 LISTAR TODAS
    // =========================
    public List<Calificacion> listarCalificaciones() {
        return calificacionRepository.findAll();
    }

    // =========================
    // 🔍 POR ID
    // =========================
    public Calificacion obtenerCalificacionPorId(Long id) {
        return calificacionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Calificación no encontrada"));
    }

    // =========================
    // ✏️ ACTUALIZAR
    // =========================
    public Calificacion actualizarCalificacion(Long id, Calificacion c) {

        Calificacion actual = obtenerCalificacionPorId(id);

        actual.setNota(c.getNota());
        actual.setObservacion(c.getObservacion());
        actual.setExamen(c.getExamen());
        actual.setEstudiante(c.getEstudiante());

        return calificacionRepository.save(actual);
    }

    // =========================
    // ❌ ELIMINAR
    // =========================
    public void eliminarCalificacion(Long id) {
        Calificacion c = obtenerCalificacionPorId(id);
        calificacionRepository.delete(c);
    }

    // =========================
    // 👨‍🎓 POR ESTUDIANTE
    // =========================
    public List<Calificacion> listarPorEstudiante(Long estudianteId) {
        return calificacionRepository.findByEstudianteId(estudianteId);
    }

    // =========================
    // 📝 POR EXAMEN
    // =========================
    public List<Calificacion> listarPorExamen(Long examenId) {
        return calificacionRepository.findByExamenId(examenId);
    }

    // =========================
    // 🏆 RANKING POR EXAMEN (PRO)
    // =========================
    public List<Calificacion> rankingPorExamen(Long examenId) {
        return calificacionRepository.rankingPorExamen(examenId);
    }

    // =========================
    // 🔥 TOP 5 POR EXAMEN
    // =========================
    public List<Calificacion> top5PorExamen(Long examenId) {
        return calificacionRepository.top5PorExamen(examenId);
    }

    // =========================
    // 🏆 RANKING GENERAL (PROMEDIO)
    // =========================
    public List<Object[]> rankingPromedioEstudiantes() {
        return calificacionRepository.rankingPromedioEstudiantes();
    }

    // =========================
    // 🔥 TOP GLOBAL
    // =========================
    public List<Calificacion> topGlobal() {
        return calificacionRepository.findTop5ByOrderByNotaDesc();
    }
}