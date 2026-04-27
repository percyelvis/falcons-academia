package com.falcons.proyecto_falcons.service;

import com.falcons.proyecto_falcons.entity.Local;
import com.falcons.proyecto_falcons.repository.LocalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class LocalService {

    @Autowired
    private LocalRepository localRepository;

    // Crear un nuevo local
    public Local crearLocal(Local local) {
        return localRepository.save(local);
    }

    // Listar todos los locales
    public List<Local> listarLocales() {
        return localRepository.findAll();
    }

    // Obtener un local por ID
    public Local obtenerLocalPorId(Long id) {
        return localRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Local no encontrado con ID: " + id));
    }

    // Actualizar un local
    public Local actualizarLocal(Long id, Local localActualizado) {
        Local localExistente = obtenerLocalPorId(id);
        localExistente.setNombre(localActualizado.getNombre());
        localExistente.setDireccion(localActualizado.getDireccion());
        localExistente.setTelefono(localActualizado.getTelefono());
        return localRepository.save(localExistente);
    }

    // Eliminar un local
    public void eliminarLocal(Long id) {
        Local local = obtenerLocalPorId(id);
        localRepository.delete(local);
    }

}
