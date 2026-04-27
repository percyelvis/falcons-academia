package com.falcons.proyecto_falcons.repository;

import com.falcons.proyecto_falcons.entity.Calificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CalificacionRepository extends JpaRepository<Calificacion, Long> {

    // =========================
    // 📌 POR ESTUDIANTE
    // =========================
    List<Calificacion> findByEstudianteId(Long estudianteId);

    // =========================
    // 📌 POR EXAMEN
    // =========================
    List<Calificacion> findByExamenId(Long examenId);

    // =========================
    // 🏆 RANKING POR EXAMEN (ENTIDADES COMPLETAS ✔)
    // =========================
    @Query("""
        SELECT c
        FROM Calificacion c
        JOIN FETCH c.estudiante e
        JOIN FETCH e.usuario
        WHERE c.examen.id = :examenId
        ORDER BY c.nota DESC
    """)
    List<Calificacion> rankingPorExamen(@Param("examenId") Long examenId);

    // =========================
    // 🔥 TOP 5 POR EXAMEN
    // =========================
    @Query(value = """
        SELECT *
        FROM calificacion
        WHERE examen_id = :examenId
        ORDER BY nota DESC
        LIMIT 5
    """, nativeQuery = true)
    List<Calificacion> top5PorExamen(@Param("examenId") Long examenId);

    // =========================
    // 🏆 RANKING GENERAL (PROMEDIO POR ESTUDIANTE)
    // =========================
    @Query("""
        SELECT 
            c.estudiante.id,
            CONCAT(c.estudiante.usuario.nombres, ' ', c.estudiante.usuario.apellidos),
            AVG(c.nota)
        FROM Calificacion c
        GROUP BY 
            c.estudiante.id,
            c.estudiante.usuario.nombres,
            c.estudiante.usuario.apellidos
        ORDER BY AVG(c.nota) DESC
    """)
    List<Object[]> rankingPromedioEstudiantes();

    // =========================
    // 🏆 TOP GLOBAL (NOTAS)
    // =========================
    List<Calificacion> findTop5ByOrderByNotaDesc();
}