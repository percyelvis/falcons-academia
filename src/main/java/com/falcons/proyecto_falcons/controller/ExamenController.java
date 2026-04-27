package com.falcons.proyecto_falcons.controller;

import com.falcons.proyecto_falcons.entity.Examen;
import com.falcons.proyecto_falcons.service.ExamenService;
import com.falcons.proyecto_falcons.service.CursoService;
import com.falcons.proyecto_falcons.service.DocenteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/examenes")
public class ExamenController {

    @Autowired
    private ExamenService examenService;

    @Autowired
    private CursoService cursoService;

    @Autowired
    private DocenteService docenteService;

    // 🔥 TODO EN UNA SOLA VISTA
    @GetMapping
    public String listar(Model model) {

        model.addAttribute("examenes", examenService.listarExamenes());
        model.addAttribute("cursos", cursoService.listarCursos());   // 🔥 CLAVE
        model.addAttribute("docentes", docenteService.listarDocentes()); // 🔥 CLAVE
        model.addAttribute("examen", new Examen());

        return "adminstrador/examenes";
    }

    // GUARDAR
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Examen examen) {
        examenService.crearExamen(examen);
        return "redirect:/examenes";
    }

    // ACTUALIZAR
    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id, @ModelAttribute Examen examen) {
        examenService.actualizarExamen(id, examen);
        return "redirect:/examenes";
    }

    // ELIMINAR
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        examenService.eliminarExamen(id);
        return "redirect:/examenes";
    }
}