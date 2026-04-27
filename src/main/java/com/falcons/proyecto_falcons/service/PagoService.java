package com.falcons.proyecto_falcons.service;

import com.falcons.proyecto_falcons.entity.Estudiante;
import com.falcons.proyecto_falcons.entity.Pago;
import com.falcons.proyecto_falcons.repository.EstudianteRepository;
import com.falcons.proyecto_falcons.repository.PagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private EstudianteRepository estudianteRepository;

    // =========================
    // 💰 CREAR PAGO
    // =========================
    public Pago crearPago(Pago pago, Long estudianteId) {

        if (estudianteId != null) {
            Estudiante estudiante = estudianteRepository.findById(estudianteId)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Estudiante no encontrado con ID: " + estudianteId));

            pago.setEstudiante(estudiante);
        }

        return pagoRepository.save(pago);
    }

    // =========================
    // 📋 LISTAR TODOS
    // =========================
    public List<Pago> listarPagos() {
        return pagoRepository.findAll();
    }

    // =========================
    // 🔍 BUSCAR POR ID
    // =========================
    public Pago obtenerPagoPorId(Long id) {
        return pagoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Pago no encontrado con ID: " + id));
    }

    // =========================
    // ✏️ ACTUALIZAR
    // =========================
    public Pago actualizarPago(Long id, Pago pagoActualizado, Long estudianteId) {

        Pago pagoExistente = obtenerPagoPorId(id);

        pagoExistente.setMonto(pagoActualizado.getMonto());
        pagoExistente.setFechaPago(pagoActualizado.getFechaPago());
        pagoExistente.setMetodo(pagoActualizado.getMetodo());
        pagoExistente.setEstado(pagoActualizado.getEstado());

        if (estudianteId != null) {
            Estudiante estudiante = estudianteRepository.findById(estudianteId)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Estudiante no encontrado con ID: " + estudianteId));

            pagoExistente.setEstudiante(estudiante);
        }

        return pagoRepository.save(pagoExistente);
    }

    // =========================
    // 🗑 ELIMINAR
    // =========================
    public void eliminarPago(Long id) {
        Pago pago = obtenerPagoPorId(id);
        pagoRepository.delete(pago);
    }

    // =========================
    // 👤 PAGOS POR ESTUDIANTE
    // =========================
    public List<Pago> listarPagosPorEstudiante(Long estudianteId) {

        Estudiante estudiante = estudianteRepository.findById(estudianteId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Estudiante no encontrado con ID: " + estudianteId));

        return pagoRepository.findByEstudiante(estudiante);
    }
}