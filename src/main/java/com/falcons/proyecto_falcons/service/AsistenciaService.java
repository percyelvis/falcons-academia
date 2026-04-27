package com.falcons.proyecto_falcons.service;

import com.falcons.proyecto_falcons.entity.Asistencia;
import com.falcons.proyecto_falcons.entity.Estudiante;
import com.falcons.proyecto_falcons.repository.AsistenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AsistenciaService {

    @Autowired
    private AsistenciaRepository repository;

    // =========================
    // 📋 LISTAR TODAS
    // =========================
    public List<Asistencia> listar() {
        return repository.findAll();
    }

    // =========================
    // 📅 LISTAR POR FECHA
    // =========================
    public List<Asistencia> listarPorFecha(LocalDate fecha) {
        return repository.findByFecha(fecha);
    }

    // =========================
    // 💾 GUARDAR / ACTUALIZAR
    // =========================
    public Asistencia guardar(Asistencia asistencia) {

        if (asistencia.getEstudiante() == null || asistencia.getEstudiante().getId() == null) {
            throw new IllegalArgumentException("Estudiante obligatorio");
        }

        if (asistencia.getEstado() == null || asistencia.getEstado().isBlank()) {
            throw new IllegalArgumentException("Estado obligatorio");
        }

        String estado = asistencia.getEstado().trim().toUpperCase();

        // 🔥 ESTADOS PERMITIDOS
        switch (estado) {
            case "PRESENTE":
            case "AUSENTE":
            case "TARDANZA":
            case "PERMISO":
                break;
            default:
                throw new IllegalArgumentException(
                        "Estado inválido: " + estado +
                                " (solo: PRESENTE, AUSENTE, TARDANZA, PERMISO)"
                );
        }

        asistencia.setEstado(estado);

        if (asistencia.getFecha() == null) {
            asistencia.setFecha(LocalDate.now());
        }

        // =========================
        // 🔥 EVITAR DUPLICADOS
        // =========================
        Optional<Asistencia> existente =
                repository.findByEstudianteAndFecha(
                        asistencia.getEstudiante(),
                        asistencia.getFecha()
                );

        if (existente.isPresent()) {
            Asistencia a = existente.get();
            a.setEstado(estado);
            return repository.save(a);
        }

        return repository.save(asistencia);
    }

    // =========================
    // 🔍 OBTENER POR ID
    // =========================
    public Asistencia obtener(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asistencia no encontrada"));
    }

    // =========================
    // ❌ ELIMINAR
    // =========================
    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    // =========================
    // 🔍 BUSCAR POR ESTUDIANTE Y FECHA
    // =========================
    public Optional<Asistencia> buscarPorEstudianteYFecha(Estudiante e, LocalDate fecha) {
        return repository.findByEstudianteAndFecha(e, fecha);
    }
}