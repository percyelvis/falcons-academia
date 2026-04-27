package com.falcons.proyecto_falcons.controller;

import com.falcons.proyecto_falcons.entity.Docente;
import com.falcons.proyecto_falcons.entity.Estudiante;
import com.falcons.proyecto_falcons.entity.Usuario;
import com.falcons.proyecto_falcons.service.DocenteService;
import com.falcons.proyecto_falcons.service.EstudianteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/docentes")
public class DocenteController {

    @Autowired
    private DocenteService service;
    @Autowired
    private EstudianteService estudianteService;

    // 📋 LISTAR (UN SOLO HTML)
    @GetMapping
    public String listar(Model model) {
        List<Docente> lista = service.listarDocentes();

        model.addAttribute("docentes", lista);
        model.addAttribute("docente", new Docente()); // para modal crear

        return "adminstrador/docentes"; // 🔥 SOLO UN HTML
    }

    // 💾 GUARDAR (crear y editar en el mismo form)
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Docente docente) {
        service.crearDocente(docente);
        return "redirect:/docentes";
    }

    // ❌ ELIMINAR
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        service.eliminarDocente(id);
        return "redirect:/docentes";
    }

    // 🔍 OBTENER PARA EDITAR (opcional si usas modal con fetch o JS)
    @GetMapping("/obtener/{id}")
    @ResponseBody
    public Docente obtener(@PathVariable Long id) {
        return service.obtenerDocentePorId(id);
    }

    @GetMapping("/mis-docentes")
    public String misDocentes(Model model, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuario == null) return "redirect:/login";

        Estudiante estudiante = estudianteService.obtenerPorUsuario(usuario);

        if (estudiante == null) return "redirect:/login";

        // 📌 sacamos cursos desde horarios del salón
        List<Docente> docentes = estudiante.getSalon()
                .getHorarios()
                .stream()
                .map(h -> h.getCurso().getDocente())
                .distinct()
                .toList();

        model.addAttribute("docentes", docentes);

        return "estudiante/misDocentes";
    }
}