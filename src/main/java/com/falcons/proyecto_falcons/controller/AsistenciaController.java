package com.falcons.proyecto_falcons.controller;

import com.falcons.proyecto_falcons.entity.Asistencia;
import com.falcons.proyecto_falcons.entity.Estudiante;
import com.falcons.proyecto_falcons.repository.EstudianteRepository;
import com.falcons.proyecto_falcons.service.AsistenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@Controller
@RequestMapping("/asistencia")
public class AsistenciaController {

    @Autowired
    private AsistenciaService asistenciaService;

    @Autowired
    private EstudianteRepository estudianteRepository;

    // 📋 LISTAR PÁGINA
    @GetMapping
    public String listar(Model model) {

        LocalDate hoy = LocalDate.now();

        List<Estudiante> estudiantes = estudianteRepository.findAll();
        List<Asistencia> asistenciasHoy = asistenciaService.listarPorFecha(hoy);

        if (asistenciasHoy == null) {
            asistenciasHoy = new ArrayList<>();
        }

        Map<Long, String> estadoMap = new HashMap<>();

        for (Asistencia a : asistenciasHoy) {
            if (a.getEstudiante() != null) {
                estadoMap.put(a.getEstudiante().getId(), a.getEstado());
            }
        }

        model.addAttribute("estudiantes", estudiantes);
        model.addAttribute("estadoMap", estadoMap);
        model.addAttribute("hoy", hoy);

        return "adminstrador/asistencia";
    }

    // ✔ MARCAR ASISTENCIA (CORRECTO)
    @PostMapping("/marcar")
    public String marcar(@RequestParam Long estudianteId,
                         @RequestParam String estado) {

        Estudiante estudiante = estudianteRepository.findById(estudianteId)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));

        Asistencia asistencia = asistenciaService
                .buscarPorEstudianteYFecha(estudiante, LocalDate.now())
                .orElse(new Asistencia());

        asistencia.setEstudiante(estudiante);
        asistencia.setEstado(estado.toUpperCase());
        asistencia.setFecha(LocalDate.now());

        asistenciaService.guardar(asistencia);

        return "redirect:/asistencia";
    }

    // =========================
// 👨‍🏫 ASISTENCIA TUTOR (NUEVO)
// =========================

}