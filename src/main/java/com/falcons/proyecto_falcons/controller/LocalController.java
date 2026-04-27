package com.falcons.proyecto_falcons.controller;

import com.falcons.proyecto_falcons.entity.Local;
import com.falcons.proyecto_falcons.service.LocalService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/locales")
public class LocalController {

    @Autowired
    private LocalService localService;

    // LISTAR
    @GetMapping
    public String listarLocales(Model model) {
        List<Local> lista = localService.listarLocales();
        model.addAttribute("locales", lista);
        return "adminstrador/locales";
    }

    // FORMULARIO CREAR
    @GetMapping("/nuevo")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("local", new Local());
        return "local/formulario";
    }

    // GUARDAR
    @PostMapping("/guardar")
    public String guardarLocal(@ModelAttribute Local local) {
        localService.crearLocal(local);
        return "redirect:/locales";
    }

    // FORMULARIO EDITAR
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Local local = localService.obtenerLocalPorId(id);
        model.addAttribute("local", local);
        return "local/formulario";
    }

    // ACTUALIZAR
    @PostMapping("/actualizar/{id}")
    public String actualizarLocal(@PathVariable Long id,
                                  @ModelAttribute Local local) {

        localService.actualizarLocal(id, local);
        return "redirect:/locales";
    }

    // ELIMINAR
    @GetMapping("/eliminar/{id}")
    public String eliminarLocal(@PathVariable Long id) {
        localService.eliminarLocal(id);
        return "redirect:/locales";
    }
}