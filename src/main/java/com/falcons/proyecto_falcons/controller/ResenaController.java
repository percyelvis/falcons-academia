package com.falcons.proyecto_falcons.controller;

import com.falcons.proyecto_falcons.entity.Resena;
import com.falcons.proyecto_falcons.service.ResenaService;
import com.falcons.proyecto_falcons.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/resenas")
public class ResenaController {

    @Autowired
    private ResenaService resenaService;

    @Autowired
    private UsuarioService usuarioService;

    // 🔹 LISTAR
    @GetMapping
    public String listar(Model model) {
        List<Resena> resenas = resenaService.listarResenas();
        model.addAttribute("resenas", resenas);
        return "resena/lista";
    }

    // 🔹 FORM NUEVO
    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("resena", new Resena());
        model.addAttribute("usuarios", usuarioService.listarUsuarios()); // 🔥 importante
        return "resena/form";
    }

    // 🔹 GUARDAR
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Resena resena,
                          @RequestParam(required = false) Long usuarioId) {

        resenaService.crearResena(resena, usuarioId);
        return "redirect:/resenas";
    }

    // 🔹 EDITAR
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Resena resena = resenaService.obtenerResenaPorId(id);
        model.addAttribute("resena", resena);
        model.addAttribute("usuarios", usuarioService.listarUsuarios());
        return "resena/form";
    }

    // 🔹 ACTUALIZAR
    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id,
                             @ModelAttribute Resena resena,
                             @RequestParam(required = false) Long usuarioId) {

        resenaService.actualizarResena(id, resena, usuarioId);
        return "redirect:/resenas";
    }

    // 🔹 ELIMINAR
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        resenaService.eliminarResena(id);
        return "redirect:/resenas";
    }
}