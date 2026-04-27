package com.falcons.proyecto_falcons.controller;

import com.falcons.proyecto_falcons.entity.Curso;
import com.falcons.proyecto_falcons.entity.Estudiante;
import com.falcons.proyecto_falcons.entity.Horario;
import com.falcons.proyecto_falcons.entity.Usuario;
import com.falcons.proyecto_falcons.service.*;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cursos")
public class CursoController {

    @Autowired
    private CursoService service;

    @Autowired
    private DocenteService docenteService;

    @Autowired
    private CicloService cicloService;
    @Autowired
    private EstudianteService estudianteService;
    @Autowired
    private CursoService cursoService;
    @Autowired
    private HorarioService horarioService;

    // ================= LISTAR =================
    @GetMapping
    public String listar(Model model) {

        model.addAttribute("cursos", service.listarCursos());
        model.addAttribute("curso", new Curso());

        // 🔥 NECESARIO PARA LOS SELECT
        model.addAttribute("docentes", docenteService.listarDocentes());
        model.addAttribute("ciclos", cicloService.listarCiclos());

        return "adminstrador/cursos";
    }

    // ================= GUARDAR =================
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Curso c) {
        service.crearCurso(c);
        return "redirect:/cursos";
    }

    // ================= ELIMINAR =================
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        service.eliminarCurso(id);
        return "redirect:/cursos";
    }

    // ================= EDITAR (opcional si usas modal) =================
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {

        Curso curso = service.obtenerCursoPorId(id);

        model.addAttribute("curso", curso);
        model.addAttribute("docentes", docenteService.listarDocentes());
        model.addAttribute("ciclos", cicloService.listarCiclos());

        return "cursos/form";
    }

    // ================= ACTUALIZAR =================
    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id, @ModelAttribute Curso curso) {
        service.actualizarCurso(id, curso);
        return "redirect:/cursos";
    }

    @GetMapping("/mis-cursos")
    public String verMisCursos(Model model, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuario == null) {
            return "redirect:/login";
        }

        Estudiante estudiante = estudianteService.obtenerPorUsuario(usuario);

        if (estudiante == null || estudiante.getSalon() == null) {
            return "redirect:/dashboard/estudiante";
        }

        Long salonId = estudiante.getSalon().getId();

        // 🔥 sacamos cursos desde los horarios del salón
        List<Curso> cursos = horarioService.listarHorarios()
                .stream()
                .filter(h -> h.getSalon().getId().equals(salonId))
                .map(Horario::getCurso)
                .distinct()
                .toList();

        model.addAttribute("cursos", cursos);
        model.addAttribute("estudiante", estudiante);

        return "estudiante/misCursos";
    }

}