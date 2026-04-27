package com.falcons.proyecto_falcons.service;

import com.falcons.proyecto_falcons.entity.Curso;
import com.falcons.proyecto_falcons.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    // =========================
    // 📌 CREAR CURSO
    // =========================
    public Curso crearCurso(Curso curso) {
        return cursoRepository.save(curso);
    }

    // =========================
    // 📌 LISTAR CURSOS
    // =========================
    public List<Curso> listarCursos() {
        return cursoRepository.findAll();
    }

    // =========================
    // 📌 OBTENER POR ID
    // =========================
    public Curso obtenerCursoPorId(Long id) {
        return cursoRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Curso no encontrado con ID: " + id));
    }

    // =========================
    // 📌 BUSCAR POR ID (ALIAS PARA CONTROLADORES)
    // =========================
    public Curso buscarPorId(Long id) {
        return obtenerCursoPorId(id);
    }

    // =========================
    // 📌 ACTUALIZAR CURSO
    // =========================
    public Curso actualizarCurso(Long id, Curso cursoActualizado) {

        Curso cursoExistente = obtenerCursoPorId(id);

        cursoExistente.setNombre(cursoActualizado.getNombre());
        cursoExistente.setDocente(cursoActualizado.getDocente());
        cursoExistente.setCiclo(cursoActualizado.getCiclo());

        return cursoRepository.save(cursoExistente);
    }

    // =========================
    // 📌 ELIMINAR CURSO
    // =========================
    public void eliminarCurso(Long id) {
        Curso curso = obtenerCursoPorId(id);
        cursoRepository.delete(curso);
    }
}