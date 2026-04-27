package com.falcons.proyecto_falcons.service;

import com.falcons.proyecto_falcons.entity.Estudiante;
import com.falcons.proyecto_falcons.entity.Usuario;
import com.falcons.proyecto_falcons.repository.EstudianteRepository;
import com.falcons.proyecto_falcons.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EstudianteService {

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // =========================
    // 🎓 CREAR
    // =========================
    public Estudiante crearEstudiante(Estudiante estudiante) {

        if (estudiante.getUsuario() == null) {
            throw new IllegalArgumentException("Debe registrar datos de usuario");
        }

        Usuario usuario = estudiante.getUsuario();

        usuario.setUsername(generarUsername(usuario.getNombres(), usuario.getDni()));
        usuario.setPassword(generarPassword(usuario.getNombres(), usuario.getDni()));
        usuario.setRol("ESTUDIANTE");

        usuario = usuarioRepository.save(usuario);

        estudiante.setUsuario(usuario);

        return estudianteRepository.save(estudiante);
    }

    // =========================
    // 📋 LISTAR
    // =========================
    public List<Estudiante> listarEstudiantes() {
        return estudianteRepository.findAll();
    }

    // =========================
    // 🔍 POR ID
    // =========================
    public Estudiante obtenerEstudiantePorId(Long id) {
        return estudianteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Estudiante no encontrado"));
    }

    // =========================
    // ✏️ ACTUALIZAR
    // =========================
    public Estudiante actualizarEstudiante(Long id, Estudiante estudianteActualizado) {

        Estudiante est = obtenerEstudiantePorId(id);

        // ===== USUARIO =====
        if (estudianteActualizado.getUsuario() != null) {

            Usuario u = est.getUsuario();
            Usuario nuevo = estudianteActualizado.getUsuario();

            u.setDni(nuevo.getDni());
            u.setNombres(nuevo.getNombres());
            u.setApellidos(nuevo.getApellidos());
            u.setEmail(nuevo.getEmail());
            u.setTelefono(nuevo.getTelefono());
            u.setDireccion(nuevo.getDireccion());
        }

        // ===== ESTUDIANTE =====
        est.setFechaNacimiento(estudianteActualizado.getFechaNacimiento());
        est.setTelefono(estudianteActualizado.getTelefono());
        est.setNumeroApoderado(estudianteActualizado.getNumeroApoderado());
        est.setDireccion(estudianteActualizado.getDireccion());

        // ===== SALÓN =====
        if (estudianteActualizado.getSalon() != null &&
                estudianteActualizado.getSalon().getId() != null) {

            est.setSalon(estudianteActualizado.getSalon());
        }

        return estudianteRepository.save(est);
    }

    // =========================
    // ❌ ELIMINAR
    // =========================
    public void eliminarEstudiante(Long id) {

        Estudiante est = obtenerEstudiantePorId(id);

        Usuario usuario = est.getUsuario();

        estudianteRepository.delete(est);
        usuarioRepository.delete(usuario);
    }

    // =========================
    // 🔎 BUSCAR DNI
    // =========================
    public List<Estudiante> buscarPorDni(String dni) {
        return estudianteRepository.findByUsuario_DniContaining(dni);
    }

    // =========================
    // ⚡ GENERADORES
    // =========================
    private String generarUsername(String nombres, String dni) {
        String limpio = nombres.toLowerCase().replaceAll("\\s+", "");
        String ultimos = dni.substring(Math.max(dni.length() - 3, 0));
        return limpio + ultimos;
    }

    private String generarPassword(String nombres, String dni) {
        return dni + nombres.substring(0, 1).toUpperCase();
    }

    public Estudiante obtenerPorUsuario(Usuario usuario) {
        return estudianteRepository.findByUsuario(usuario);
    }

    public List<Estudiante> listarPorSalon(Long salonId) {
        return estudianteRepository.findBySalonId(salonId);
    }
}