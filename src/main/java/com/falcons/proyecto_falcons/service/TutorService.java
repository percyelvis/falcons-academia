package com.falcons.proyecto_falcons.service;

import com.falcons.proyecto_falcons.entity.Tutor;
import com.falcons.proyecto_falcons.entity.Usuario;
import com.falcons.proyecto_falcons.repository.TutorRepository;
import com.falcons.proyecto_falcons.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TutorService {

    @Autowired
    private TutorRepository tutorRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Crear un tutor
    public Tutor crearTutor(Tutor tutor) {
        if (tutor.getUsuario() == null)
            throw new IllegalArgumentException("El tutor debe tener un usuario asociado");

        // Verificar que el usuario exista y no tenga ya un tutor asociado
        Usuario usuario = usuarioRepository.findById(tutor.getUsuario().getId())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + tutor.getUsuario().getId()));

        if (usuario.getTutor() != null)
            throw new IllegalArgumentException("Este usuario ya está asociado a un tutor");

        tutor.setUsuario(usuario);
        return tutorRepository.save(tutor);
    }

    // Listar todos los tutores
    public List<Tutor> listarTutores() {
        return tutorRepository.findAll();
    }

    // Obtener tutor por ID
    public Tutor obtenerTutorPorId(Long id) {
        return tutorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tutor no encontrado con ID: " + id));
    }

    // Actualizar tutor
    public Tutor actualizarTutor(Long id, Tutor tutorActualizado) {
        Tutor tutorExistente = obtenerTutorPorId(id);

        tutorExistente.setTelefono(tutorActualizado.getTelefono());
        // Solo se permite cambiar el usuario si es necesario y no tiene otro tutor
        if (tutorActualizado.getUsuario() != null && !tutorExistente.getUsuario().getId().equals(tutorActualizado.getUsuario().getId())) {
            Usuario nuevoUsuario = usuarioRepository.findById(tutorActualizado.getUsuario().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + tutorActualizado.getUsuario().getId()));
            if (nuevoUsuario.getTutor() != null)
                throw new IllegalArgumentException("Este usuario ya está asociado a otro tutor");
            tutorExistente.setUsuario(nuevoUsuario);
        }

        return tutorRepository.save(tutorExistente);
    }

    // Eliminar tutor
    public void eliminarTutor(Long id) {
        Tutor tutor = obtenerTutorPorId(id);
        tutorRepository.delete(tutor);
    }

    public Tutor obtenerPorUsuario(Usuario usuario) {
        return tutorRepository.findByUsuario(usuario)
                .orElseThrow(() -> new IllegalArgumentException("Tutor no encontrado"));
    }

    public int contarEstudiantesPorTutor(Tutor tutor) {
        return tutor.getSalones()
                .stream()
                .mapToInt(s -> s.getEstudiantes() != null ? s.getEstudiantes().size() : 0)
                .sum();
    }
}
