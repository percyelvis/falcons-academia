package com.falcons.proyecto_falcons.service;

import com.falcons.proyecto_falcons.entity.Egresado;
import com.falcons.proyecto_falcons.entity.Estudiante;
import com.falcons.proyecto_falcons.repository.EgresadoRepository;
import com.falcons.proyecto_falcons.repository.EstudianteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EgresadoService {

    @Autowired
    private EgresadoRepository egresadoRepository;

    @Autowired
    private EstudianteRepository estudianteRepository;

    // ================= CREAR =================
    public Egresado crearEgresado(Egresado egresado) {

        if (egresado.getEstudiante() == null || egresado.getEstudiante().getId() == null) {
            throw new IllegalArgumentException("El estudiante es obligatorio");
        }

        Estudiante estudiante = estudianteRepository.findById(
                egresado.getEstudiante().getId()
        ).orElseThrow(() -> new IllegalArgumentException("Estudiante no encontrado"));

        // 🔥 evitar duplicados (1 egresado por estudiante)
        if (egresadoRepository.findAll()
                .stream()
                .anyMatch(e -> e.getEstudiante().getId().equals(estudiante.getId()))) {
            throw new IllegalArgumentException("Este estudiante ya está registrado como egresado");
        }

        egresado.setEstudiante(estudiante);

        return egresadoRepository.save(egresado);
    }

    // ================= LISTAR =================
    public List<Egresado> listarEgresados() {
        return egresadoRepository.findAll();
    }

    // ================= OBTENER =================
    public Egresado obtenerEgresadoPorId(Long id) {
        return egresadoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Egresado no encontrado"));
    }

    // ================= ACTUALIZAR =================
    public Egresado actualizarEgresado(Long id, Egresado egresadoActualizado) {

        Egresado existente = obtenerEgresadoPorId(id);

        existente.setFechaEgreso(egresadoActualizado.getFechaEgreso());
        existente.setInstitucionDestino(egresadoActualizado.getInstitucionDestino());
        existente.setCarrera(egresadoActualizado.getCarrera());
        existente.setObservaciones(egresadoActualizado.getObservaciones());

        if (egresadoActualizado.getEstudiante() != null &&
                egresadoActualizado.getEstudiante().getId() != null) {

            Estudiante estudiante = estudianteRepository.findById(
                    egresadoActualizado.getEstudiante().getId()
            ).orElseThrow(() -> new IllegalArgumentException("Estudiante no encontrado"));

            existente.setEstudiante(estudiante);
        }

        return egresadoRepository.save(existente);
    }

    // ================= ELIMINAR =================
    public void eliminarEgresado(Long id) {
        Egresado egresado = obtenerEgresadoPorId(id);
        egresadoRepository.delete(egresado);
    }
}