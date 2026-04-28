package com.falcons.proyecto_falcons.service;

import com.falcons.proyecto_falcons.entity.Usuario;
import com.falcons.proyecto_falcons.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // =========================
    // 🔥 CREAR USUARIO
    // =========================
    public Usuario crearUsuario(Usuario usuario) {

        validarBase(usuario);

        if (usuario.getTelefono() == null) usuario.setTelefono("");
        if (usuario.getDireccion() == null) usuario.setDireccion("");

        if (usuarioRepository.existsByDni(usuario.getDni()))
            throw new IllegalArgumentException("Ya existe un usuario con este DNI");

        // 🔥 USERNAME AUTOMÁTICO
        if (usuario.getUsername() == null || usuario.getUsername().isBlank()) {
            usuario.setUsername(
                    usuario.getNombres().toLowerCase().replaceAll("\\s+", "") +
                            usuario.getDni().substring(usuario.getDni().length() - 3)
            );
        }

        if (usuarioRepository.existsByUsername(usuario.getUsername()))
            throw new IllegalArgumentException("Ya existe un username");

        // 🔐 PASSWORD AUTOMÁTICA (SIN ENCRIPTAR)
        if (usuario.getPassword() == null || usuario.getPassword().isBlank()) {
            usuario.setPassword(generarPassword(usuario.getDni(), usuario.getNombres()));
        }

        return usuarioRepository.save(usuario);
    }

    // =========================
    // 🔐 GENERAR PASSWORD
    // =========================
    private String generarPassword(String dni, String nombres) {

        String inicial = "";

        if (nombres != null && !nombres.isBlank()) {
            inicial = nombres.substring(0, 1).toUpperCase();
        }

        return dni + inicial;
    }

    // =========================
    // 📋 LISTAR
    // =========================
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    // =========================
    // 🔍 POR ID
    // =========================
    public Usuario obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
    }

    // =========================
    // ✏️ ACTUALIZAR
    // =========================
    public Usuario actualizarUsuario(Long id, Usuario usuarioActualizado) {

        Usuario u = obtenerUsuarioPorId(id);

        u.setDni(usuarioActualizado.getDni());
        u.setNombres(usuarioActualizado.getNombres());
        u.setApellidos(usuarioActualizado.getApellidos());
        u.setEmail(usuarioActualizado.getEmail());

        u.setTelefono(usuarioActualizado.getTelefono() != null ? usuarioActualizado.getTelefono() : "");
        u.setDireccion(usuarioActualizado.getDireccion() != null ? usuarioActualizado.getDireccion() : "");

        // USERNAME
        if (usuarioActualizado.getUsername() != null && !usuarioActualizado.getUsername().isBlank()) {
            u.setUsername(usuarioActualizado.getUsername());
        }

        // PASSWORD (SIN ENCRIPTAR)
        if (usuarioActualizado.getPassword() != null && !usuarioActualizado.getPassword().isBlank()) {
            u.setPassword(usuarioActualizado.getPassword());
        }

        // ROL
        if (usuarioActualizado.getRol() != null && !usuarioActualizado.getRol().isBlank()) {
            u.setRol(usuarioActualizado.getRol());
        }

        return usuarioRepository.save(u);
    }

    // =========================
    // ❌ ELIMINAR
    // =========================
    public void eliminarUsuario(Long id) {
        usuarioRepository.delete(obtenerUsuarioPorId(id));
    }

    // =========================
    // 🔐 LOGIN
    // =========================
    public Usuario validarCredenciales(String username, String password, String rol) {

        Usuario u = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // ✅ comparación simple
        if (!password.equals(u.getPassword())) {
            throw new IllegalArgumentException("Contraseña incorrecta");
        }

        if (!u.getRol().equalsIgnoreCase(rol)) {
            throw new IllegalArgumentException("Rol incorrecto");
        }

        return u;
    }

    // =========================
    // 🔒 VALIDACIÓN BASE
    // =========================
    private void validarBase(Usuario usuario) {

        if (usuario.getDni() == null || usuario.getDni().isBlank())
            throw new IllegalArgumentException("El DNI es obligatorio");

        if (usuario.getNombres() == null || usuario.getNombres().isBlank())
            throw new IllegalArgumentException("El nombre es obligatorio");

        if (usuario.getRol() == null || usuario.getRol().isBlank())
            throw new IllegalArgumentException("El rol es obligatorio");
    }

    // =========================
    // 🔑 RESET PASSWORD
    // =========================
    public String resetPassword(Long id) {

        Usuario u = obtenerUsuarioPorId(id);

        String nueva = u.getDni() + u.getNombres().substring(0, 1).toUpperCase();

        u.setPassword(nueva);

        usuarioRepository.save(u);

        return nueva;
    }

    // =========================
    // 📊 COUNT
    // =========================
    public long contarUsuarios() {
        return usuarioRepository.count();
    }
}