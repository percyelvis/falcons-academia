package com.falcons.proyecto_falcons.repository;

import com.falcons.proyecto_falcons.entity.Estudiante;
import com.falcons.proyecto_falcons.entity.Matricula;
import com.falcons.proyecto_falcons.entity.Salon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatriculaRepository extends JpaRepository<Matricula, Long> {
    List<Matricula> findByEstudiante(Estudiante estudiante);
    List<Matricula> findBySalon(Salon salon);
}
