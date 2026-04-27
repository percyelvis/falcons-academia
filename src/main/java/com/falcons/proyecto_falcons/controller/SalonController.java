package com.falcons.proyecto_falcons.controller;

import com.falcons.proyecto_falcons.entity.Salon;
import com.falcons.proyecto_falcons.service.LocalService;
import com.falcons.proyecto_falcons.service.SalonService;
import com.falcons.proyecto_falcons.service.TutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/salones")
public class SalonController {

    @Autowired
    private SalonService salonService;

    @Autowired
    private TutorService tutorService;

    @Autowired
    private LocalService localService;

    // ================= LISTAR =================
    @GetMapping
    public String listar(Model model) {

        model.addAttribute("salones", salonService.listarSalones());

        model.addAttribute("tutores", tutorService.listarTutores());
        model.addAttribute("locales", localService.listarLocales());

        return "adminstrador/salones";
    }

    // ================= FORM NUEVO =================
    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("salon", new Salon());
        model.addAttribute("tutores", tutorService.listarTutores());
        model.addAttribute("locales", localService.listarLocales());
        return "adminstrador/salon-form";
    }

    // ================= GUARDAR =================
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Salon salon,
                          @RequestParam(required = false) Long tutorId,
                          @RequestParam(required = false) Long localId) {

        salonService.crearSalon(salon, tutorId, localId);
        return "redirect:/salones";
    }

    // ================= EDITAR =================
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("salon", salonService.obtenerSalonPorId(id));
        model.addAttribute("tutores", tutorService.listarTutores());
        model.addAttribute("locales", localService.listarLocales());
        return "adminstrador/salon-form";
    }

    // ================= ACTUALIZAR =================
    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id,
                             @ModelAttribute Salon salon,
                             @RequestParam(required = false) Long tutorId,
                             @RequestParam(required = false) Long localId) {

        salonService.actualizarSalon(id, salon, tutorId, localId);
        return "redirect:/salones";
    }

    // ================= ELIMINAR =================
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        salonService.eliminarSalon(id);
        return "redirect:/salones";
    }


}