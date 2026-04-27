package com.falcons.proyecto_falcons.service;

import com.falcons.proyecto_falcons.entity.Docente;
import com.falcons.proyecto_falcons.repository.DocenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DocenteService {

    private final DocenteRepository docenteRepository;

    @Autowired
    public DocenteService(DocenteRepository docenteRepository) {
        this.docenteRepository = docenteRepository;
    }

    // 🔥 CREAR
    public Docente crearDocente(Docente docente) {
        if (docente == null) {
            throw new IllegalArgumentException("El docente no puede ser nulo");
        }

        if (docente.getNombre() == null || docente.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre del docente es obligatorio");
        }

        return docenteRepository.save(docente);
    }

    // 📋 LISTAR
    public List<Docente> listarDocentes() {
        return docenteRepository.findAll();
    }

    // 🔍 OBTENER POR ID
    public Docente obtenerDocentePorId(Long id) {
        return docenteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Docente no encontrado con ID: " + id));
    }

    // ✏️ ACTUALIZAR
    public Docente actualizarDocente(Long id, Docente docenteActualizado) {

        if (docenteActualizado == null) {
            throw new IllegalArgumentException("El docente actualizado no puede ser nulo");
        }

        Docente docenteExistente = obtenerDocentePorId(id);

        // 🔥 ACTUALIZAR CAMPOS NUEVOS
        docenteExistente.setNombre(docenteActualizado.getNombre());
        docenteExistente.setEspecialidad(docenteActualizado.getEspecialidad());
        docenteExistente.setTelefono(docenteActualizado.getTelefono());

        return docenteRepository.save(docenteExistente);
    }

    // ❌ ELIMINAR
    public void eliminarDocente(Long id) {
        Docente docente = obtenerDocentePorId(id);
        docenteRepository.delete(docente);
    }

    // 📊 CONTADOR (para dashboard)
    public long contarDocentes() {
        return docenteRepository.count();
    }
}