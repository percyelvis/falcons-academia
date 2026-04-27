package com.falcons.proyecto_falcons.service;

import com.falcons.proyecto_falcons.entity.Examen;
import com.falcons.proyecto_falcons.repository.ExamenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ExamenService {

    @Autowired
    private ExamenRepository examenRepository;

    // Crear un examen
    public Examen crearExamen(Examen examen) {
        return examenRepository.save(examen);
    }

    // Listar todos los exámenes
    public List<Examen> listarExamenes() {
        return examenRepository.findAll();
    }

    // Obtener examen por ID
    public Examen obtenerExamenPorId(Long id) {
        return examenRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Examen no encontrado con ID: " + id));
    }

    // Actualizar examen
    public Examen actualizarExamen(Long id, Examen examenActualizado) {
        Examen examenExistente = obtenerExamenPorId(id);
        examenExistente.setTitulo(examenActualizado.getTitulo());
        examenExistente.setDescripcion(examenActualizado.getDescripcion());
        examenExistente.setFecha(examenActualizado.getFecha());
        examenExistente.setDuracionMinutos(examenActualizado.getDuracionMinutos());
        examenExistente.setTipo(examenActualizado.getTipo());
        examenExistente.setCurso(examenActualizado.getCurso());
        examenExistente.setDocente(examenActualizado.getDocente());
        return examenRepository.save(examenExistente);
    }

    // Eliminar examen
    public void eliminarExamen(Long id) {
        Examen examen = obtenerExamenPorId(id);
        examenRepository.delete(examen);
    }
}
