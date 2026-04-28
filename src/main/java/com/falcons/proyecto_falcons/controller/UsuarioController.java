package com.falcons.proyecto_falcons.controller;

import com.falcons.proyecto_falcons.entity.Usuario;
import com.falcons.proyecto_falcons.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("usuarios", service.listarUsuarios());
        return "usuarios/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "usuarios/form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Usuario usuario) {

        // 🔥 FIX IMPORTANTE: evitar username null
        if (usuario.getUsername() == null || usuario.getUsername().isBlank()) {
            usuario.setUsername(usuario.getDni());
        }

        service.crearUsuario(usuario);
        return "redirect:/usuarios";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("usuario", service.obtenerUsuarioPorId(id));
        return "usuarios/form";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id,
                             @ModelAttribute Usuario usuario) {

        // 🔥 FIX CRÍTICO
        if (usuario.getUsername() == null || usuario.getUsername().isBlank()) {
            Usuario actual = service.obtenerUsuarioPorId(id);
            usuario.setUsername(actual.getUsername());
        }

        service.actualizarUsuario(id, usuario);
        return "redirect:/usuarios";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        service.eliminarUsuario(id);
        return "redirect:/usuarios";
    }

    @GetMapping("/count")
    @ResponseBody
    public long contarUsuarios() {
        return service.listarUsuarios().size();
    }

    @PostMapping("/reset/{id}")
    @ResponseBody
    public String resetPassword(@PathVariable Long id) {
        return service.resetPassword(id);
    }


}