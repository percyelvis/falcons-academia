package com.falcons.proyecto_falcons.repository;

import com.falcons.proyecto_falcons.entity.Tutor;
import com.falcons.proyecto_falcons.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TutorRepository extends JpaRepository<Tutor, Long> {

    Optional<Tutor> findByUsuario(Usuario usuario);
}
