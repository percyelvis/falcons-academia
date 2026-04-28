package com.falcons.proyecto_falcons.controller;

import com.falcons.proyecto_falcons.entity.Estudiante;
import com.falcons.proyecto_falcons.entity.Tutor;
import com.falcons.proyecto_falcons.entity.Usuario;
import com.falcons.proyecto_falcons.service.*;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EstudianteService estudianteService;

    @Autowired
    private CursoService cursoService;

    @Autowired
    private DocenteService docenteService;

    @Autowired
    private ExamenService examenService;

    @Autowired
    private CalificacionService calificacionService;

    @Autowired
    private TutorService tutorService;

    // =========================
    // 🔐 LOGIN
    // =========================
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        @RequestParam String rol,
                        HttpSession session,
                        RedirectAttributes redirectAttributes) {

        try {

            Usuario usuario = usuarioService.validarCredenciales(username, password, rol);

            if (usuario == null) {
                redirectAttributes.addFlashAttribute("error", "Credenciales incorrectas");
                return "redirect:/login";
            }

            session.setAttribute("usuarioLogueado", usuario);

            String r = usuario.getRol().trim().toUpperCase();

            switch (r) {

                case "ADMINISTRADOR":
                    return "redirect:/dashboard";

                case "TUTOR":
                    return "redirect:/dashboard/tutor";

                case "ESTUDIANTE":
                    return "redirect:/dashboard/estudiante";

                default:
                    redirectAttributes.addFlashAttribute("error", "Rol no válido");
                    return "redirect:/login";
            }

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/login";
        }
    }

    // =========================
    // 📄 LOGIN VIEW
    // =========================
    @GetMapping("/login")
    public String loginPage() {
        return "adminstrador/login";
    }

    // =========================
    // 📊 DASHBOARD ADMIN
    // =========================
    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        int totalEstudiantes = 0;
        int totalDocentes = 0;
        int totalCursos = 0;

        try {
            totalEstudiantes = estudianteService.listarEstudiantes().size();
        } catch (Exception e) {
            System.out.println("Error estudiantes: " + e.getMessage());
        }

        try {
            totalDocentes = docenteService.listarDocentes().size();
        } catch (Exception e) {
            System.out.println("Error docentes: " + e.getMessage());
        }

        try {
            totalCursos = cursoService.listarCursos().size();
        } catch (Exception e) {
            System.out.println("Error cursos: " + e.getMessage());
        }

        model.addAttribute("totalEstudiantes", totalEstudiantes);
        model.addAttribute("totalDocentes", totalDocentes);
        model.addAttribute("totalCursos", totalCursos);

        return "adminstrador/dashboard";
    }

    // =========================
    // 📊 DASHBOARD TUTOR
    // =========================
    @GetMapping("/dashboard/tutor")
    public String dashboardTutor(Model model, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuario == null) {
            return "redirect:/login";
        }

        Tutor tutor = tutorService.obtenerPorUsuario(usuario);

        if (tutor == null) {
            return "redirect:/login";
        }

        // 🔥 cálculo seguro de estudiantes
        int totalEstudiantes = 0;

        if (tutor.getSalones() != null) {
            totalEstudiantes = tutor.getSalones()
                    .stream()
                    .filter(s -> s.getEstudiantes() != null)
                    .mapToInt(s -> s.getEstudiantes().size())
                    .sum();
        }

        model.addAttribute("tutor", tutor);
        model.addAttribute("totalEstudiantes", totalEstudiantes);

        return "tutor/dashboardTutor";
    }

    // =========================
    // 📊 DASHBOARD ESTUDIANTE
    // =========================
    @GetMapping("/dashboard/estudiante")
    public String dashboardEstudiante(Model model, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuario == null) return "redirect:/login";

        Estudiante estudiante = estudianteService.obtenerPorUsuario(usuario);

        if (estudiante == null) return "redirect:/login";

        Long id = estudiante.getId();

        var examen = examenService.listarExamenes()
                .stream()
                .reduce((first, second) -> second)
                .orElse(null);

        if (examen != null) {

            var ranking = calificacionService.rankingPorExamen(examen.getId());

            int posicion = 0;
            double puntaje = 0;

            for (int i = 0; i < ranking.size(); i++) {

                var c = ranking.get(i);

                if (c.getEstudiante().getId().equals(id)) {
                    posicion = i + 1;
                    puntaje = c.getNota();
                    break;
                }
            }

            model.addAttribute("ranking", posicion);
            model.addAttribute("puntaje", puntaje);
            model.addAttribute("totalEstudiantes", ranking.size());

        } else {
            model.addAttribute("ranking", "-");
            model.addAttribute("puntaje", "-");
            model.addAttribute("totalEstudiantes", 0);
        }

        if (examen != null) {
            model.addAttribute("proximoCurso", examen.getCurso().getNombre());
            model.addAttribute("fechaExamen", examen.getFecha());
        } else {
            model.addAttribute("proximoCurso", "-");
            model.addAttribute("fechaExamen", "-");
        }

        model.addAttribute("progreso",
                calificacionService.listarPorEstudiante(id)
                        .stream()
                        .map(c -> c.getNota())
                        .toList());

        model.addAttribute("nombre", usuario.getNombres());
        model.addAttribute("apellido", usuario.getApellidos());

        return "estudiante/dashboardEstudiante";
    }

    // =========================
    // 📚 PREMATRÍCULA
    // =========================


    // =========================
    // 👨‍🎓 CONTROL
    // =========================
    @GetMapping("/control-estudiantes")
    public String controlEstudiantes() {
        return "administrador/control_estudiantes";
    }

    @GetMapping("/nosotros")
    public String nosotros() {
        return "/nosotros";
    }

    // =========================
    // 🚪 LOGOUT
    // =========================
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @Controller
    public class HomeController {

        @GetMapping("/")
        public String home() {
            return "Index"; // templates/index.html
        }
    }
}