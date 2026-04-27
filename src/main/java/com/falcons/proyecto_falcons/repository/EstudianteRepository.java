package com.falcons.proyecto_falcons.repository;

import com.falcons.proyecto_falcons.entity.Estudiante;
import com.falcons.proyecto_falcons.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {
    List<Estudiante> findByUsuario_DniContaining(String dni);

    Estudiante findByUsuario(Usuario usuario);

    @Query("""
    SELECT 
        c.estudiante.id,
        c.estudiante.usuario.nombres,
        AVG(c.nota)
    FROM Calificacion c
    GROUP BY c.estudiante.id, c.estudiante.usuario.nombres
    ORDER BY AVG(c.nota) DESC
""")
    List<Object[]> rankingPromedioEstudiantes();

    List<Estudiante> findBySalonId(Long salonId);
}
