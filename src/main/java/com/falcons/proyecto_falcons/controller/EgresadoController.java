package com.falcons.proyecto_falcons.controller;

import com.falcons.proyecto_falcons.entity.Egresado;
import com.falcons.proyecto_falcons.entity.Estudiante;
import com.falcons.proyecto_falcons.service.EgresadoService;
import com.falcons.proyecto_falcons.service.EstudianteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/egresados")
public class EgresadoController {

    @Autowired
    private EgresadoService egresadoService;

    @Autowired
    private EstudianteService estudianteService;

    // ================= LISTAR =================
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("egresados", egresadoService.listarEgresados());
        model.addAttribute("estudiantes", estudianteService.listarEstudiantes());
        return "adminstrador/egresados";
    }

    // ================= GUARDAR =================
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Egresado egresado,
                          @RequestParam("estudianteId") Long estudianteId,
                          Model model) {

        try {
            Estudiante estudiante = estudianteService.obtenerEstudiantePorId(estudianteId);
            egresado.setEstudiante(estudiante);

            egresadoService.crearEgresado(egresado);

            return "redirect:/egresados";

        } catch (Exception e) {

            model.addAttribute("error", e.getMessage());
            model.addAttribute("egresados", egresadoService.listarEgresados());
            model.addAttribute("estudiantes", estudianteService.listarEstudiantes());

            return "adminstrador/egresados";
        }
    }

    // ================= FORM EDITAR =================
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {

        Egresado egresado = egresadoService.obtenerEgresadoPorId(id);

        model.addAttribute("egresado", egresado);
        model.addAttribute("estudiantes", estudianteService.listarEstudiantes());

        return "adminstrador/egresados-edit";
    }

    // ================= ACTUALIZAR =================
    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id,
                             @ModelAttribute Egresado egresado,
                             @RequestParam(value = "estudianteId", required = false) Long estudianteId,
                             Model model) {

        try {

            if (estudianteId != null) {
                Estudiante estudiante = estudianteService.obtenerEstudiantePorId(estudianteId);
                egresado.setEstudiante(estudiante);
            }

            egresadoService.actualizarEgresado(id, egresado);

            return "redirect:/egresados";

        } catch (Exception e) {

            model.addAttribute("error", e.getMessage());
            model.addAttribute("egresados", egresadoService.listarEgresados());
            model.addAttribute("estudiantes", estudianteService.listarEstudiantes());

            return "adminstrador/egresados";
        }
    }

    // ================= ELIMINAR =================
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        egresadoService.eliminarEgresado(id);
        return "redirect:/egresados";
    }
}