package com.falcons.proyecto_falcons.service;

import com.falcons.proyecto_falcons.entity.Ciclo;
import com.falcons.proyecto_falcons.repository.CicloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CicloService {

    @Autowired
    private CicloRepository cicloRepository;

    // Crear un nuevo ciclo
    public Ciclo crearCiclo(Ciclo ciclo) {
        return cicloRepository.save(ciclo);
    }

    // Listar todos los ciclos
    public List<Ciclo> listarCiclos() {
        return cicloRepository.findAll();
    }

    // Obtener ciclo por ID
    public Ciclo obtenerCicloPorId(Long id) {
        return cicloRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ciclo no encontrado con ID: " + id));
    }

    // Actualizar ciclo
    public Ciclo actualizarCiclo(Long id, Ciclo cicloActualizado) {
        Ciclo cicloExistente = obtenerCicloPorId(id);
        cicloExistente.setNombre(cicloActualizado.getNombre());
        cicloExistente.setFechaInicio(cicloActualizado.getFechaInicio());
        cicloExistente.setFechaFin(cicloActualizado.getFechaFin());
        // Los cursos asociados se gestionan mediante CursoService
        return cicloRepository.save(cicloExistente);
    }

    // Eliminar ciclo
    public void eliminarCiclo(Long id) {
        Ciclo ciclo = obtenerCicloPorId(id);
        cicloRepository.delete(ciclo);
    }
}

