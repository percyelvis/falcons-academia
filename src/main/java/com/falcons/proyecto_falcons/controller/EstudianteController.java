package com.falcons.proyecto_falcons.controller;

import com.falcons.proyecto_falcons.entity.Estudiante;
import com.falcons.proyecto_falcons.entity.PreMatricula;
import com.falcons.proyecto_falcons.entity.Usuario;
import com.falcons.proyecto_falcons.service.EstudianteService;
import com.falcons.proyecto_falcons.service.PreMatriculaService;
import com.falcons.proyecto_falcons.service.UsuarioService;
import com.falcons.proyecto_falcons.service.SalonService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/estudiantes")
public class EstudianteController {

    @Autowired
    private EstudianteService estudianteService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private SalonService salonService;
    @Autowired
    private PreMatriculaService preMatriculaService;

    // =========================
    // 📋 LISTAR (ADMIN)
    // =========================
    @GetMapping
    public String listarEstudiantes(@RequestParam(value = "dni", required = false) String dni,
                                    Model model) {

        List<Estudiante> lista;

        if (dni != null && !dni.isEmpty()) {
            lista = estudianteService.buscarPorDni(dni);
        } else {
            lista = estudianteService.listarEstudiantes();
        }

        model.addAttribute("estudiantes", lista);
        model.addAttribute("salones", salonService.listarSalones());

        return "adminstrador/estudiantes";
    }

    // =========================
    // ➕ GUARDAR
    // =========================
    @PostMapping("/guardar")
    public String guardarEstudiante(@ModelAttribute Estudiante estudiante,
                                    RedirectAttributes redirect) {

        try {
            estudianteService.crearEstudiante(estudiante);
            redirect.addFlashAttribute("success", "Estudiante registrado correctamente");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "DNI ya registrado o datos duplicados");
        }

        return "redirect:/estudiantes";
    }

    // =========================
    // ✏️ EDITAR
    // =========================
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {

        Estudiante estudiante = estudianteService.obtenerEstudiantePorId(id);

        model.addAttribute("estudiante", estudiante);
        model.addAttribute("usuarios", usuarioService.listarUsuarios());
        model.addAttribute("salones", salonService.listarSalones());

        return "estudiante/formulario";
    }

    // =========================
    // 🔄 ACTUALIZAR
    // =========================
    @PostMapping("/actualizar/{id}")
    public String actualizarEstudiante(@PathVariable Long id,
                                       @ModelAttribute Estudiante estudiante,
                                       RedirectAttributes redirect) {

        try {
            estudianteService.actualizarEstudiante(id, estudiante);
            redirect.addFlashAttribute("success", "Estudiante actualizado correctamente");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Error al actualizar estudiante");
        }

        return "redirect:/estudiantes";
    }

    // =========================
    // ❌ ELIMINAR
    // =========================
    @GetMapping("/eliminar/{id}")
    public String eliminarEstudiante(@PathVariable Long id,
                                     RedirectAttributes redirect) {

        try {
            estudianteService.eliminarEstudiante(id);
            redirect.addFlashAttribute("success", "Estudiante eliminado correctamente");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Error al eliminar estudiante");
        }

        return "redirect:/estudiantes";
    }

    // =========================
    // 📊 COUNT
    // =========================
    @GetMapping("/count")
    @ResponseBody
    public long contarEstudiantes() {
        return estudianteService.listarEstudiantes().size();
    }

    // =========================
    // 👁 DETALLE ESTUDIANTE (JSON)
    // =========================
    @GetMapping("/detalle/{id}")
    @ResponseBody
    public Map<String, Object> detalleEstudiante(@PathVariable Long id) {

        Estudiante e = estudianteService.obtenerEstudiantePorId(id);

        Map<String, Object> data = new HashMap<>();

        data.put("id", e.getId());
        data.put("fechaNacimiento", e.getFechaNacimiento());
        data.put("telefono", e.getTelefono());
        data.put("numeroApoderado", e.getNumeroApoderado());
        data.put("direccion", e.getDireccion());

        Map<String, Object> u = new HashMap<>();
        u.put("dni", e.getUsuario().getDni());
        u.put("nombres", e.getUsuario().getNombres());
        u.put("apellidos", e.getUsuario().getApellidos());
        u.put("email", e.getUsuario().getEmail());
        u.put("telefono", e.getUsuario().getTelefono());
        u.put("direccion", e.getUsuario().getDireccion());
        u.put("username", e.getUsuario().getUsername());
        u.put("password", e.getUsuario().getPassword());

        data.put("usuario", u);

        return data;
    }

    @PostMapping("/desde-prematricula/{id}")
    @ResponseBody
    public String matricularDesdePrematricula(@PathVariable Long id) {

        // 1. buscar prematricula
        PreMatricula p = preMatriculaService.obtenerPorId(id);

        // 2. convertir a estudiante
        Estudiante e = new Estudiante();

        Usuario u = new Usuario();
        u.setNombres(p.getNombres());
        u.setApellidos(p.getApellidos());
        u.setDni(p.getDni());
        u.setEmail(p.getCorreo());
        u.setTelefono(p.getTelefono());
        u.setRol("ESTUDIANTE");

        e.setUsuario(u);

        estudianteService.crearEstudiante(e);

        // 3. eliminar prematricula (opcional pero recomendado)
        preMatriculaService.eliminar(id);

        return "OK";
    }

}