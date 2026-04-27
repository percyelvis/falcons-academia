package com.falcons.proyecto_falcons.service;

import com.falcons.proyecto_falcons.entity.Estudiante;
import com.falcons.proyecto_falcons.entity.Matricula;
import com.falcons.proyecto_falcons.entity.Salon;
import com.falcons.proyecto_falcons.repository.EstudianteRepository;
import com.falcons.proyecto_falcons.repository.MatriculaRepository;
import com.falcons.proyecto_falcons.repository.SalonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MatriculaService {

    @Autowired
    private MatriculaRepository matriculaRepository;

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Autowired
    private SalonRepository salonRepository;

    // Crear una matrícula asociando estudiante y salón
    public Matricula crearMatricula(Matricula matricula, Long estudianteId, Long salonId) {
        Estudiante estudiante = estudianteRepository.findById(estudianteId)
                .orElseThrow(() -> new IllegalArgumentException("Estudiante no encontrado con ID: " + estudianteId));

        Salon salon = salonRepository.findById(salonId)
                .orElseThrow(() -> new IllegalArgumentException("Salón no encontrado con ID: " + salonId));

        matricula.setEstudiante(estudiante);
        matricula.setSalon(salon);

        return matriculaRepository.save(matricula);
    }

    // Listar todas las matrículas
    public List<Matricula> listarMatriculas() {
        return matriculaRepository.findAll();
    }

    // Obtener matrícula por ID
    public Matricula obtenerMatriculaPorId(Long id) {
        return matriculaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Matrícula no encontrada con ID: " + id));
    }

    // Actualizar matrícula
    public Matricula actualizarMatricula(Long id, Matricula matriculaActualizada, Long estudianteId, Long salonId) {
        Matricula matriculaExistente = obtenerMatriculaPorId(id);

        matriculaExistente.setFechaMatricula(matriculaActualizada.getFechaMatricula());
        matriculaExistente.setEstado(matriculaActualizada.getEstado());

        if (estudianteId != null) {
            Estudiante estudiante = estudianteRepository.findById(estudianteId)
                    .orElseThrow(() -> new IllegalArgumentException("Estudiante no encontrado con ID: " + estudianteId));
            matriculaExistente.setEstudiante(estudiante);
        }

        if (salonId != null) {
            Salon salon = salonRepository.findById(salonId)
                    .orElseThrow(() -> new IllegalArgumentException("Salón no encontrado con ID: " + salonId));
            matriculaExistente.setSalon(salon);
        }

        return matriculaRepository.save(matriculaExistente);
    }

    // Eliminar matrícula
    public void eliminarMatricula(Long id) {
        Matricula matricula = obtenerMatriculaPorId(id);
        matriculaRepository.delete(matricula);
    }

    // Listar matrículas de un estudiante
    public List<Matricula> listarMatriculasPorEstudiante(Long estudianteId) {
        Estudiante estudiante = estudianteRepository.findById(estudianteId)
                .orElseThrow(() -> new IllegalArgumentException("Estudiante no encontrado con ID: " + estudianteId));
        return matriculaRepository.findByEstudiante(estudiante);
    }

    // Listar matrículas de un salón
    public List<Matricula> listarMatriculasPorSalon(Long salonId) {
        Salon salon = salonRepository.findById(salonId)
                .orElseThrow(() -> new IllegalArgumentException("Salón no encontrado con ID: " + salonId));
        return matriculaRepository.findBySalon(salon);
    }
}
