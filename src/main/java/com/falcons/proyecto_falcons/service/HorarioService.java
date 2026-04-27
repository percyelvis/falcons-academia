package com.falcons.proyecto_falcons.service;

import com.falcons.proyecto_falcons.entity.Horario;
import com.falcons.proyecto_falcons.repository.HorarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class HorarioService {

    @Autowired
    private HorarioRepository horarioRepository;

    // =========================
    // 📌 CREAR
    // =========================
    public Horario crearHorario(Horario horario) {
        return horarioRepository.save(horario);
    }

    // =========================
    // 📌 LISTAR TODOS
    // =========================
    public List<Horario> listarHorarios() {
        return horarioRepository.findAll();
    }

    // =========================
    // 📌 LISTAR POR SALÓN (🔥 CLAVE PARA ESTUDIANTE)
    // =========================
    public List<Horario> listarPorSalon(Long id) {
        return horarioRepository.findBySalonIdOrderByDiaAsc(id);
    }

    // =========================
    // 📌 OBTENER POR ID
    // =========================
    public Horario obtenerHorarioPorId(Long id) {
        return horarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Horario no encontrado con ID: " + id));
    }

    // =========================
    // 📌 ACTUALIZAR
    // =========================
    public Horario actualizarHorario(Long id, Horario horarioActualizado) {

        Horario h = obtenerHorarioPorId(id);

        h.setDia(horarioActualizado.getDia());
        h.setHoraInicio(horarioActualizado.getHoraInicio());
        h.setHoraFin(horarioActualizado.getHoraFin());

        h.setCurso(horarioActualizado.getCurso());
        h.setDocente(horarioActualizado.getDocente());
        h.setSalon(horarioActualizado.getSalon());

        return horarioRepository.save(h);
    }

    // =========================
    // 📌 ELIMINAR
    // =========================
    public void eliminarHorario(Long id) {
        Horario horario = obtenerHorarioPorId(id);
        horarioRepository.delete(horario);
    }
}