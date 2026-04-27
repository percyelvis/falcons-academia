package com.falcons.proyecto_falcons.service;

import com.falcons.proyecto_falcons.entity.Local;
import com.falcons.proyecto_falcons.entity.Salon;
import com.falcons.proyecto_falcons.entity.Tutor;
import com.falcons.proyecto_falcons.repository.LocalRepository;
import com.falcons.proyecto_falcons.repository.SalonRepository;
import com.falcons.proyecto_falcons.repository.TutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SalonService {

    @Autowired
    private SalonRepository salonRepository;

    @Autowired
    private TutorRepository tutorRepository;

    @Autowired
    private LocalRepository localRepository;

    // Crear un salon
    public Salon crearSalon(Salon salon, Long tutorId, Long localId) {
        if (tutorId != null) {
            Tutor tutor = tutorRepository.findById(tutorId)
                    .orElseThrow(() -> new IllegalArgumentException("Tutor no encontrado con ID: " + tutorId));
            salon.setTutor(tutor);
        }

        if (localId != null) {
            Local local = localRepository.findById(localId)
                    .orElseThrow(() -> new IllegalArgumentException("Local no encontrado con ID: " + localId));
            salon.setLocal(local);
        }

        return salonRepository.save(salon);
    }

    // Listar todos los salones
    public List<Salon> listarSalones() {
        return salonRepository.findAll();
    }

    // Obtener un salon por ID
    public Salon obtenerSalonPorId(Long id) {
        return salonRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Salon no encontrado con ID: " + id));
    }

    // Actualizar un salon
    public Salon actualizarSalon(Long id, Salon salonActualizado, Long tutorId, Long localId) {
        Salon salonExistente = obtenerSalonPorId(id);

        salonExistente.setNombre(salonActualizado.getNombre());
        salonExistente.setCapacidad(salonActualizado.getCapacidad());

        if (tutorId != null) {
            Tutor tutor = tutorRepository.findById(tutorId)
                    .orElseThrow(() -> new IllegalArgumentException("Tutor no encontrado con ID: " + tutorId));
            salonExistente.setTutor(tutor);
        }

        if (localId != null) {
            Local local = localRepository.findById(localId)
                    .orElseThrow(() -> new IllegalArgumentException("Local no encontrado con ID: " + localId));
            salonExistente.setLocal(local);
        }

        return salonRepository.save(salonExistente);
    }

    // Eliminar un salon
    public void eliminarSalon(Long id) {
        Salon salon = obtenerSalonPorId(id);
        salonRepository.delete(salon);
    }

    public Salon buscarPorId(Long id) {
        return obtenerSalonPorId(id);
    }
}
