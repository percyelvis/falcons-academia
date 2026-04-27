package com.falcons.proyecto_falcons.controller;

import com.falcons.proyecto_falcons.entity.Ciclo;
import com.falcons.proyecto_falcons.service.CicloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/ciclos")
public class CicloController {

    @Autowired
    private CicloService cicloService;

    // ================= LISTAR =================
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("ciclos", cicloService.listarCiclos());
        model.addAttribute("ciclo", new Ciclo());
        return "adminstrador/ciclos"; // 🔥 IMPORTANTE
    }

    // ================= GUARDAR =================
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Ciclo ciclo) {
        cicloService.crearCiclo(ciclo);
        return "redirect:/ciclos";
    }

    // ================= ELIMINAR =================
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        cicloService.eliminarCiclo(id);
        return "redirect:/ciclos";
    }

    // ================= ACTUALIZAR =================
    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id, @ModelAttribute Ciclo ciclo) {
        cicloService.actualizarCiclo(id, ciclo);
        return "redirect:/ciclos";
    }
}