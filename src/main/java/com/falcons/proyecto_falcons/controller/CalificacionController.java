package com.falcons.proyecto_falcons.controller;

import com.falcons.proyecto_falcons.entity.Calificacion;
import com.falcons.proyecto_falcons.entity.Estudiante;
import com.falcons.proyecto_falcons.entity.Usuario;
import com.falcons.proyecto_falcons.repository.CalificacionRepository;
import com.falcons.proyecto_falcons.repository.EstudianteRepository;
import com.falcons.proyecto_falcons.service.CalificacionService;
import com.falcons.proyecto_falcons.service.EstudianteService;
import com.falcons.proyecto_falcons.service.ExamenService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/calificaciones")
public class CalificacionController {

    @Autowired
    private CalificacionService calificacionService;

    @Autowired
    private ExamenService examenService;

    @Autowired
    private EstudianteService estudianteService;
    @Autowired
    private EstudianteRepository estudianteRepository;
    @Autowired
    private CalificacionRepository calificacionRepository;

    // =========================
    // 📋 ADMIN PRINCIPAL
    // =========================
    @GetMapping
    public String listar(Model model) {

        model.addAttribute("calificaciones",
                calificacionService.listarCalificaciones());

        model.addAttribute("examenes",
                examenService.listarExamenes());

        model.addAttribute("estudiantes",
                estudianteService.listarEstudiantes());

        // 🏆 TOP 5 DEL ÚLTIMO EXAMEN
        var ultimoExamen = examenService.listarExamenes()
                .stream()
                .max(Comparator.comparingLong(e -> e.getId()))
                .orElse(null);

        if (ultimoExamen != null) {
            model.addAttribute("ranking",
                    calificacionService.top5PorExamen(ultimoExamen.getId()));
        }

        return "adminstrador/calificacion";
    }

    // =========================
    // ➕ GUARDAR INDIVIDUAL
    // =========================
    @PostMapping("/guardar")
    public String guardar(
            @RequestParam Long examenId,
            @RequestParam Long estudianteId,
            @RequestParam Double nota,
            @RequestParam(required = false) String observacion
    ) {

        Calificacion c = new Calificacion();
        c.setExamen(examenService.obtenerExamenPorId(examenId));
        c.setEstudiante(estudianteService.obtenerEstudiantePorId(estudianteId));
        c.setNota(nota);
        c.setObservacion(observacion);

        calificacionService.crearCalificacion(c);

        return "redirect:/calificaciones";
    }

    // =========================
    // 🚀 GUARDADO MASIVO
    // =========================
    @PostMapping("/guardar-masivo")
    public String guardarMasivo(
            @RequestParam Long examenId,
            @RequestParam List<Long> estudianteIds,
            @RequestParam List<Double> notas,
            @RequestParam(required = false) List<String> observaciones
    ) {

        for (int i = 0; i < estudianteIds.size(); i++) {

            Double nota = notas.get(i);
            if (nota == null) continue;

            Calificacion c = new Calificacion();
            c.setExamen(examenService.obtenerExamenPorId(examenId));
            c.setEstudiante(estudianteService.obtenerEstudiantePorId(estudianteIds.get(i)));
            c.setNota(nota);

            if (observaciones != null && observaciones.size() > i) {
                c.setObservacion(observaciones.get(i));
            }

            calificacionService.crearCalificacion(c);
        }

        return "redirect:/calificaciones";
    }

    // =========================
    // ❌ ELIMINAR
    // =========================
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {

        calificacionService.eliminarCalificacion(id);

        return "redirect:/calificaciones";
    }

    // =========================
    // 🏆 RANKING POR EXAMEN (ADMIN)
    // =========================

    // =========================
    // 🌐 RANKING PÚBLICO (INDEX)
    // =========================
    @GetMapping("/ranking")
    public String rankingPublico(Model model) {

        var ultimoExamen = examenService.listarExamenes()
                .stream()
                .max(Comparator.comparingLong(e -> e.getId()))
                .orElse(null);

        if (ultimoExamen == null) {
            model.addAttribute("ranking", List.of());
            model.addAttribute("examen", null);
            return "publico/ranking"; // 👈 IMPORTANTE
        }

        model.addAttribute("examen", ultimoExamen);
        model.addAttribute("ranking",
                calificacionService.rankingPorExamen(ultimoExamen.getId()));

        return "ranking"; // 👈 IMPORTANTE
    }

    // =========================
    // 🏆 RANKING GENERAL (PROMEDIO ESTUDIANTES)
    // =========================
    @GetMapping("/mi-ranking")
    public String rankingGeneral(Model model, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuario == null) {
            return "redirect:/login";
        }

        List<Object[]> ranking = calificacionService.rankingPromedioEstudiantes();

        model.addAttribute("ranking", ranking);

        Long miId = estudianteService.obtenerPorUsuario(usuario).getId();

        int posicion = 1;

        for (Object[] fila : ranking) {
            Long idEstudiante = ((Number) fila[0]).longValue();

            if (idEstudiante.equals(miId)) break;

            posicion++;
        }

        model.addAttribute("miPosicion", posicion);

        return "estudiante/miRanking";
    }



    @GetMapping("/mis-calificaciones")
    public String misCalificaciones(Model model, HttpSession session) {

        // 🔐 USUARIO CORRECTO
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuario == null) {
            return "redirect:/login";
        }

        Estudiante estudiante = estudianteRepository.findByUsuario(usuario);

        if (estudiante == null) {
            return "redirect:/login";
        }

        List<Calificacion> calificaciones =
                calificacionRepository.findByEstudianteId(estudiante.getId());

        double promedio = calificaciones.stream()
                .mapToDouble(Calificacion::getNota)
                .average()
                .orElse(0.0);

        long aprobados = calificaciones.stream()
                .filter(c -> c.getNota() >= 13)
                .count();

        long desaprobados = calificaciones.stream()
                .filter(c -> c.getNota() < 13)
                .count();

        model.addAttribute("calificaciones", calificaciones);
        model.addAttribute("promedio", String.format("%.2f", promedio));
        model.addAttribute("aprobados", aprobados);
        model.addAttribute("desaprobados", desaprobados);

        return "estudiante/miCalificacion";
    }
}