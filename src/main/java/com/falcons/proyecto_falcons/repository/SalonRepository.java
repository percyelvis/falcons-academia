package com.falcons.proyecto_falcons.repository;

import com.falcons.proyecto_falcons.entity.Salon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalonRepository extends JpaRepository<Salon, Long> {
}
