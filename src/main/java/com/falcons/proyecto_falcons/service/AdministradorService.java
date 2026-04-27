package com.falcons.proyecto_falcons.service;

import com.falcons.proyecto_falcons.entity.Administrador;
import com.falcons.proyecto_falcons.repository.AdministradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AdministradorService {

    @Autowired
    private AdministradorRepository administradorRepository;

    // Crear administrador
    public Administrador crearAdministrador(Administrador administrador) {
        return administradorRepository.save(administrador);
    }

    // Listar todos los administradores
    public List<Administrador> listarAdministradores() {
        return administradorRepository.findAll();
    }

    // Obtener administrador por ID
    public Administrador obtenerAdministradorPorId(Long id) {
        return administradorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Administrador no encontrado con ID: " + id));
    }

    // Actualizar administrador
    public Administrador actualizarAdministrador(Long id, Administrador administradorActualizado) {
        Administrador administradorExistente = obtenerAdministradorPorId(id);
        administradorExistente.setCargo(administradorActualizado.getCargo());
        administradorExistente.setUsuario(administradorActualizado.getUsuario());
        return administradorRepository.save(administradorExistente);
    }

    // Eliminar administrador
    public void eliminarAdministrador(Long id) {
        Administrador administrador = obtenerAdministradorPorId(id);
        administradorRepository.delete(administrador);
    }
}
