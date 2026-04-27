package com.falcons.proyecto_falcons.controller;

import com.falcons.proyecto_falcons.entity.Estudiante;
import com.falcons.proyecto_falcons.entity.Pago;
import com.falcons.proyecto_falcons.entity.Usuario;
import com.falcons.proyecto_falcons.service.EstudianteService;
import com.falcons.proyecto_falcons.service.PagoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/pagos")
public class PagoController {

    @Autowired
    private PagoService pagoService;
    @Autowired
    private EstudianteService estudianteService;

    // ================= LISTAR =================
    @GetMapping
    public String listar(Model model) {
        List<Pago> pagos = pagoService.listarPagos();
        List<Estudiante> estudiantes = estudianteService.listarEstudiantes(); // Asegúrate de tener este método
        model.addAttribute("pagos", pagos);
        model.addAttribute("estudiantes", estudiantes); // Enviar lista de alumnos
        return "adminstrador/pagos";
    }

    // ================= NUEVO =================
    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("pago", new Pago());
        return "pago/form";
    }

    // ================= GUARDAR =================
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Pago pago,
                          @RequestParam(required = false) Long estudianteId) {

        pagoService.crearPago(pago, estudianteId);
        return "redirect:/pagos";
    }

    // ================= EDITAR =================
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Pago pago = pagoService.obtenerPagoPorId(id);
        model.addAttribute("pago", pago);
        return "pago/form";
    }

    // ================= ACTUALIZAR =================
    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id,
                             @ModelAttribute Pago pago,
                             @RequestParam(required = false) Long estudianteId) {

        pagoService.actualizarPago(id, pago, estudianteId);
        return "redirect:/pagos";
    }

    // ================= ELIMINAR =================
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        pagoService.eliminarPago(id);
        return "redirect:/pagos";
    }

    // ================= PAGOS POR ESTUDIANTE =================
    @GetMapping("/estudiante/{id}")
    public String pagosPorEstudiante(@PathVariable Long id, Model model) {
        List<Pago> pagos = pagoService.listarPagosPorEstudiante(id);
        model.addAttribute("pagos", pagos);
        return "adminstrador/pagos";
    }

    // ================= VER COMPROBANTE (URL EXTERNA) =================
    @GetMapping("/ver/{id}")
    public String verPago(@PathVariable Long id) {

        Pago pago = pagoService.obtenerPagoPorId(id);

        // 🔥 aquí puedes cambiar tu "base online"
        String urlBase = "https://tu-storage-online.com/comprobantes/";

        // ejemplo: redirige a imagen o captura
        return "redirect:" + urlBase + pago.getId();
    }

    @GetMapping("/mis-pagos")
    public String misPagos(Model model, HttpSession session) {

        // 🔐 Usuario logueado
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");

        if (usuario == null) {
            return "redirect:/login";
        }

        // 👤 Estudiante por usuario
        Estudiante estudiante = estudianteService.obtenerPorUsuario(usuario);

        if (estudiante == null) {
            return "redirect:/login";
        }

        Long estudianteId = estudiante.getId();

        // 💳 Usamos TU service ya hecho (correcto)
        List<Pago> pagos = pagoService.listarPagosPorEstudiante(estudianteId);

        // 💰 total pagado
        double totalPagado = pagos.stream()
                .filter(p -> "PAGADO".equalsIgnoreCase(p.getEstado()))
                .mapToDouble(Pago::getMonto)
                .sum();

        // ❌ deuda
        double deuda = pagos.stream()
                .filter(p -> "PENDIENTE".equalsIgnoreCase(p.getEstado()))
                .mapToDouble(Pago::getMonto)
                .sum();

        // 📤 enviar a vista
        model.addAttribute("pagos", pagos);
        model.addAttribute("totalPagado", totalPagado);
        model.addAttribute("deuda", deuda);

        // 👤 header
        model.addAttribute("nombre", usuario.getNombres());
        model.addAttribute("apellido", usuario.getApellidos());

        return "estudiante/miPago";
    }


}