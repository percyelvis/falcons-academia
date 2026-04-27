package com.falcons.proyecto_falcons.repository;

import com.falcons.proyecto_falcons.entity.Asistencia;
import com.falcons.proyecto_falcons.entity.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {

    List<Asistencia> findByEstudianteId(Long estudianteId);

    List<Asistencia> findByFecha(LocalDate fecha);

    Optional<Asistencia> findByEstudianteAndFecha(Estudiante estudiante, LocalDate fecha);
}