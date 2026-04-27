package com.falcons.proyecto_falcons.controller;

import com.falcons.proyecto_falcons.entity.*;
import com.falcons.proyecto_falcons.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/horarios")
public class HorarioController {

    @Autowired private HorarioService horarioService;
    @Autowired private SalonService salonService;
    @Autowired private CursoService cursoService;
    @Autowired private DocenteService docenteService;
    @Autowired private EstudianteService estudianteService;

    // ================= LISTAR =================
    @GetMapping
    public String listar(Model model) {

        model.addAttribute("horarios", horarioService.listarHorarios());
        model.addAttribute("cursos", cursoService.listarCursos());
        model.addAttribute("salones", salonService.listarSalones());
        model.addAttribute("docentes", docenteService.listarDocentes());

        return "adminstrador/horarios";
    }

    // ================= GUARDAR =================
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Horario h) {

        h.setCurso(cursoService.buscarPorId(h.getCurso().getId()));
        h.setDocente(docenteService.obtenerDocentePorId(h.getDocente().getId()));
        h.setSalon(salonService.obtenerSalonPorId(h.getSalon().getId()));

        h.setHoraInicio(LocalTime.parse(h.getHoraInicio().toString()));
        h.setHoraFin(LocalTime.parse(h.getHoraFin().toString()));

        horarioService.crearHorario(h);

        return "redirect:/horarios";
    }

    // ================= ACTUALIZAR =================
    @PostMapping("/actualizar/{id}")
    public String actualizarHorario(
            @PathVariable Long id,
            @RequestParam String dia,
            @RequestParam String horaInicio,
            @RequestParam String horaFin
    ) {

        Horario h = horarioService.obtenerHorarioPorId(id);

        h.setDia(dia);
        h.setHoraInicio(LocalTime.parse(horaInicio));
        h.setHoraFin(LocalTime.parse(horaFin));

        horarioService.actualizarHorario(id, h);

        return "redirect:/horarios";
    }


    // ================= ELIMINAR =================
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        horarioService.eliminarHorario(id);
        return "redirect:/horarios";
    }

    // ================= HORARIOS ESTUDIANTE =================
    @GetMapping("/mis-horarios")
    public String misHorarios(Model model, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuario == null) return "redirect:/login";

        Estudiante estudiante = estudianteService.obtenerPorUsuario(usuario);

        if (estudiante == null || estudiante.getSalon() == null)
            return "redirect:/dashboard/estudiante";

        List<Horario> horarios = horarioService.listarPorSalon(estudiante.getSalon().getId());

        model.addAttribute("horarios", horarios);
        model.addAttribute("estudiante", estudiante);

        return "estudiante/miHorario";
    }
}