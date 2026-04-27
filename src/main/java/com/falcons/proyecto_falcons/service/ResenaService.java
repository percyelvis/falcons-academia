package com.falcons.proyecto_falcons.service;

import com.falcons.proyecto_falcons.entity.Resena;
import com.falcons.proyecto_falcons.entity.Usuario;
import com.falcons.proyecto_falcons.repository.ResenaRepository;
import com.falcons.proyecto_falcons.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ResenaService {

    @Autowired
    private ResenaRepository resenaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Crear una reseña asociada a un usuario
    public Resena crearResena(Resena resena, Long usuarioId) {
        if (usuarioId != null) {
            Usuario usuario = usuarioRepository.findById(usuarioId)
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + usuarioId));
            resena.setUsuario(usuario);
        }
        return resenaRepository.save(resena);
    }

    // Listar todas las reseñas
    public List<Resena> listarResenas() {
        return resenaRepository.findAll();
    }

    // Obtener una reseña por ID
    public Resena obtenerResenaPorId(Long id) {
        return resenaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reseña no encontrada con ID: " + id));
    }

    // Actualizar una reseña
    public Resena actualizarResena(Long id, Resena resenaActualizada, Long usuarioId) {
        Resena resenaExistente = obtenerResenaPorId(id);

        resenaExistente.setComentario(resenaActualizada.getComentario());
        resenaExistente.setCalificacion(resenaActualizada.getCalificacion());
        resenaExistente.setFecha(resenaActualizada.getFecha());

        if (usuarioId != null) {
            Usuario usuario = usuarioRepository.findById(usuarioId)
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + usuarioId));
            resenaExistente.setUsuario(usuario);
        }

        return resenaRepository.save(resenaExistente);
    }

    // Eliminar una reseña
    public void eliminarResena(Long id) {
        Resena resena = obtenerResenaPorId(id);
        resenaRepository.delete(resena);
    }
}
