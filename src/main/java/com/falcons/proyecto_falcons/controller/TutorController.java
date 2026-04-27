package com.falcons.proyecto_falcons.controller;

import com.falcons.proyecto_falcons.entity.*;
import com.falcons.proyecto_falcons.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/tutores")
public class TutorController {

    @Autowired
    private TutorService tutorService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EstudianteService estudianteService;

    @Autowired
    private SalonService salonService;

    @Autowired
    private HorarioService horarioService;

    // ================= LISTAR =================
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("tutores", tutorService.listarTutores());
        return "adminstrador/tutores";
    }

    // ================= NUEVO =================
    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("tutor", new Tutor());
        model.addAttribute("usuario", new Usuario());
        return "adminstrador/tutores";
    }

    // ================= GUARDAR =================
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Tutor tutor) {

        Usuario usuario = tutor.getUsuario();

        if (usuario == null) {
            throw new IllegalArgumentException("Usuario vacío");
        }

        usuario.setRol("TUTOR");

        if (usuario.getUsername() == null || usuario.getUsername().isBlank()) {
            usuario.setUsername(usuario.getDni());
        }

        Usuario nuevoUsuario = usuarioService.crearUsuario(usuario);
        tutor.setUsuario(nuevoUsuario);

        tutorService.crearTutor(tutor);

        return "redirect:/tutores";
    }

    // ================= EDITAR =================
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {

        Tutor tutor = tutorService.obtenerTutorPorId(id);

        model.addAttribute("tutor", tutor);
        model.addAttribute("usuario", tutor.getUsuario());

        return "adminstrador/tutores";
    }

    // ================= ACTUALIZAR =================
    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id,
                             @ModelAttribute Tutor tutor,
                             @ModelAttribute Usuario usuario,
                             Model model) {

        try {
            Tutor existente = tutorService.obtenerTutorPorId(id);
            Usuario original = existente.getUsuario();

            if (usuario.getUsername() == null || usuario.getUsername().isBlank()) {
                usuario.setUsername(original.getUsername());
            }

            usuarioService.actualizarUsuario(original.getId(), usuario);

            existente.setTelefono(tutor.getTelefono());

            tutorService.actualizarTutor(id, existente);

            return "redirect:/tutores";

        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("tutores", tutorService.listarTutores());
            return "adminstrador/tutores";
        }
    }

    // ================= ELIMINAR =================
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        tutorService.eliminarTutor(id);
        return "redirect:/tutores";
    }

    // ================= SALONES =================
    @GetMapping("/tutor/salones")
    public String verSalones(Model model, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuario == null) return "redirect:/login";

        Tutor tutor = tutorService.obtenerPorUsuario(usuario);

        if (tutor == null) {
            model.addAttribute("error", "Tutor no encontrado");
            return "error";
        }

        model.addAttribute("tutor", tutor);

        return "tutor/salonesTutor";
    }

    // ================= ESTUDIANTES (MODAL) =================
    @GetMapping("/tutor/salon/{id}")
    @ResponseBody
    public List<Estudiante> estudiantesPorSalon(@PathVariable Long id) {
        return estudianteService.listarPorSalon(id);
    }

    // =====================================================
    // 🔥 HORARIOS - VISTA GENERAL
    // =====================================================
    @GetMapping("/tutor/horarios")
    public String vistaHorariosTutor(Model model, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuario == null) return "redirect:/login";

        Tutor tutor = tutorService.obtenerPorUsuario(usuario);

        if (tutor == null) {
            model.addAttribute("error", "Tutor no encontrado");
            return "error";
        }

        model.addAttribute("salones", tutor.getSalones());

        return "tutor/horariosTutor";
    }

    // =====================================================
    // 🔥 HORARIOS POR SALÓN (HTML NORMAL)
    // =====================================================
    @GetMapping("/tutor/salon/{id}/horarios")
    public String horariosPorSalon(@PathVariable Long id, Model model, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuario == null) return "redirect:/login";

        Tutor tutor = tutorService.obtenerPorUsuario(usuario);

        if (tutor == null) {
            model.addAttribute("error", "Tutor no encontrado");
            return "error";
        }

        Salon salon = salonService.obtenerSalonPorId(id);
        List<Horario> horarios = horarioService.listarPorSalon(id);

        model.addAttribute("salon", salon);
        model.addAttribute("horarios", horarios);

        return "tutor/horariosTutor";
    }

    // =====================================================
    // 🔥 HORARIOS JSON (MODAL DINÁMICO)
    // =====================================================
    // ================= HORARIOS JSON (MODAL) =================
    @GetMapping("/tutor/salon/{id}/horarios-json")
    @ResponseBody
    public List<Horario> horariosJson(@PathVariable Long id) {
        return horarioService.listarPorSalon(id);
    }


    @GetMapping("/tutor/asistencia")
    public String asistenciaTutor(Model model, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuario == null) return "redirect:/login";

        Tutor tutor = tutorService.obtenerPorUsuario(usuario);

        if (tutor == null) {
            model.addAttribute("error", "Tutor no encontrado");
            return "error";
        }

        // 🔥 IMPORTANTE: cargar salones desde BD (NO lazy roto)
        Tutor tutorCompleto = tutorService.obtenerTutorPorId(tutor.getId());

        model.addAttribute("salones", tutorCompleto.getSalones());

        return "tutor/asistenciaTutor";
    }
}