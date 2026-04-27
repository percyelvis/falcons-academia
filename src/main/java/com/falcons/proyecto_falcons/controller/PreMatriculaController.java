package com.falcons.proyecto_falcons.controller;

import com.falcons.proyecto_falcons.entity.PreMatricula;
import com.falcons.proyecto_falcons.service.PreMatriculaService;
import com.falcons.proyecto_falcons.service.CicloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PreMatriculaController {

    @Autowired
    private PreMatriculaService preMatriculaService;

    @Autowired
    private CicloService cicloService;

    // =========================
    // 📄 FORMULARIO CLIENTE
    // =========================
    @GetMapping("/prematricula")
    public String mostrarFormulario(Model model) {

        // 🔥 ciclos desde backend
        model.addAttribute("ciclos", cicloService.listarCiclos());

        return "matricula/prematricula";
    }

    // =========================
    // 💾 GUARDAR PREMATRÍCULA
    // =========================
    @PostMapping("/prematriculas/guardar")
    public String guardar(@ModelAttribute PreMatricula preMatricula) {

        preMatriculaService.guardar(preMatricula);

        // 🔥 redirección con confirmación
        return "matricula/confirmacion";
    }

    // =========================
    // 📋 LISTAR (ADMIN)
    // =========================
    @GetMapping("/prematriculas")
    public String listar(Model model) {

        model.addAttribute("prematriculas", preMatriculaService.listar());

        return "adminstrador/prematriculas";
    }

    // =========================
    // ❌ ELIMINAR
    // =========================
    @GetMapping("/prematriculas/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {

        preMatriculaService.eliminar(id);

        return "redirect:/prematriculas";
    }

    // =========================
    // 🔍 OBTENER (AJAX / MODAL)
    // =========================
    @GetMapping("/prematriculas/obtener/{id}")
    @ResponseBody
    public PreMatricula obtener(@PathVariable Long id) {

        return preMatriculaService.obtenerPorId(id);
    }
}