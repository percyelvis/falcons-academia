package com.falcons.proyecto_falcons.controller;

import com.falcons.proyecto_falcons.entity.Matricula;
import com.falcons.proyecto_falcons.service.MatriculaService;
import com.falcons.proyecto_falcons.service.EstudianteService;
import com.falcons.proyecto_falcons.service.SalonService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/matriculas")
public class MatriculaController {

    @Autowired
    private MatriculaService matriculaService;

    @Autowired
    private EstudianteService estudianteService;

    @Autowired
    private SalonService salonService;

    // LISTAR
    @GetMapping("/listarMatriculas")
    public String listarMatriculas(Model model) {
        List<Matricula> lista = matriculaService.listarMatriculas();
        model.addAttribute("matriculas", lista);
        return "matricula/lista";
    }

    // FORMULARIO CREAR
    @GetMapping("/nuevo")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("matricula", new Matricula());
        model.addAttribute("estudiantes", estudianteService.listarEstudiantes());
        model.addAttribute("salones", salonService.listarSalones());
        return "matricula/Crear";
    }

    // GUARDAR
    @PostMapping("/guardar")
    public String guardarMatricula(@ModelAttribute Matricula matricula,
                                   @RequestParam Long estudianteId,
                                   @RequestParam Long salonId) {

        matriculaService.crearMatricula(matricula, estudianteId, salonId);
        return "redirect:/matriculas";
    }

    // EDITAR FORMULARIO
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Matricula matricula = matriculaService.obtenerMatriculaPorId(id);
        model.addAttribute("matricula", matricula);
        model.addAttribute("estudiantes", estudianteService.listarEstudiantes());
        model.addAttribute("salones", salonService.listarSalones());
        return "PreMatricula";
    }

    // ACTUALIZAR
    @PostMapping("/actualizar/{id}")
    public String actualizarMatricula(@PathVariable Long id,
                                      @ModelAttribute Matricula matricula,
                                      @RequestParam Long estudianteId,
                                      @RequestParam Long salonId) {

        matriculaService.actualizarMatricula(id, matricula, estudianteId, salonId);
        return "redirect:/matriculas";
    }

    // ELIMINAR
    @GetMapping("/eliminar/{id}")
    public String eliminarMatricula(@PathVariable Long id) {
        matriculaService.eliminarMatricula(id);
        return "redirect:/matriculas";
    }

    // FILTRAR POR ESTUDIANTE
    @GetMapping("/estudiante/{id}")
    public String listarPorEstudiante(@PathVariable Long id, Model model) {
        model.addAttribute("matriculas", matriculaService.listarMatriculasPorEstudiante(id));
        return "matricula/lista";
    }

    // FILTRAR POR SALON
    @GetMapping("/salon/{id}")
    public String listarPorSalon(@PathVariable Long id, Model model) {
        model.addAttribute("matriculas", matriculaService.listarMatriculasPorSalon(id));
        return "matricula/lista";
    }
}