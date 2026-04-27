package com.falcons.proyecto_falcons.repository;

import com.falcons.proyecto_falcons.entity.Egresado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EgresadoRepository extends JpaRepository<Egresado, Long> {
}
