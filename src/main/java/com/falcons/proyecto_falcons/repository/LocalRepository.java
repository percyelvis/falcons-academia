package com.falcons.proyecto_falcons.repository;

import com.falcons.proyecto_falcons.entity.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocalRepository extends JpaRepository<Local, Long> {
}
