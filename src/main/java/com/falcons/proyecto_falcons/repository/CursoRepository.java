package com.falcons.proyecto_falcons.repository;

import com.falcons.proyecto_falcons.entity.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {
}
