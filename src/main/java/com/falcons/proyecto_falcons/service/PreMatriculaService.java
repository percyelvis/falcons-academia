package com.falcons.proyecto_falcons.service;

import com.falcons.proyecto_falcons.entity.PreMatricula;
import com.falcons.proyecto_falcons.repository.PreMatriculaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PreMatriculaService {

    @Autowired
    private PreMatriculaRepository repository;

    public List<PreMatricula> listar() {
        return repository.findAll();
    }

    public PreMatricula guardar(PreMatricula preMatricula) {
        return repository.save(preMatricula);
    }

    public PreMatricula obtenerPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}