package com.falcons.proyecto_falcons.controller;

import com.falcons.proyecto_falcons.entity.Usuario;
import com.falcons.proyecto_falcons.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/configuracion")
public class ConfiguracionController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // =========================
    // ⚙️ VER PERFIL
    // =========================
    @GetMapping
    public String verConfiguracion(Model model, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuario == null) return "redirect:/login";

        model.addAttribute("usuario", usuario);

        return "estudiante/miConfiguracion";
    }

    // =========================
    // ✏️ ACTUALIZAR DATOS PERSONALES
    // =========================
    @PostMapping("/actualizar")
    public String actualizarDatos(@ModelAttribute Usuario form, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuario == null) return "redirect:/login";

        Usuario db = usuarioRepository.findById(usuario.getId()).orElse(null);

        if (db != null) {
            db.setNombres(form.getNombres());
            db.setApellidos(form.getApellidos());
            db.setEmail(form.getEmail());
            db.setTelefono(form.getTelefono());
            db.setDireccion(form.getDireccion());

            usuarioRepository.save(db);
            session.setAttribute("usuarioLogueado", db);
        }

        return "redirect:/configuracion";
    }

    // =========================
    // 🔐 CAMBIAR CONTRASEÑA
    // =========================
    @PostMapping("/cambiar-password")
    public String cambiarPassword(@RequestParam String actual,
                                  @RequestParam String nueva,
                                  HttpSession session,
                                  Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuario == null) return "redirect:/login";

        Usuario db = usuarioRepository.findById(usuario.getId()).orElse(null);

        if (db == null) return "redirect:/login";

        // ⚠️ VALIDAR CONTRASEÑA ACTUAL
        if (!db.getPassword().equals(actual)) {
            model.addAttribute("error", "Contraseña actual incorrecta");
            return "estudiante/miConfiguracion";
        }

        db.setPassword(nueva); // (luego lo puedes encriptar con BCrypt)
        usuarioRepository.save(db);

        session.setAttribute("usuarioLogueado", db);

        return "redirect:/configuracion";
    }
}