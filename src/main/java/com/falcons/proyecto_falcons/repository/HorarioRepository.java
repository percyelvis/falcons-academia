package com.falcons.proyecto_falcons.repository;

import com.falcons.proyecto_falcons.entity.Horario;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HorarioRepository extends JpaRepository<Horario, Long> {

    @EntityGraph(attributePaths = {
            "curso",
            "curso.docente"
    })
    List<Horario> findBySalonIdOrderByDiaAsc(Long salonId);

    List<Horario> findBySalonId(Long salonId);
}