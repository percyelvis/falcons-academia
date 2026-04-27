package com.falcons.proyecto_falcons.repository;

import com.falcons.proyecto_falcons.entity.PreMatricula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreMatriculaRepository extends JpaRepository<PreMatricula, Long> {
}