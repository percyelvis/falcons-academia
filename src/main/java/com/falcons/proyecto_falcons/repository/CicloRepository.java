package com.falcons.proyecto_falcons.repository;

import com.falcons.proyecto_falcons.entity.Ciclo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CicloRepository extends JpaRepository<Ciclo, Long> {
}
