package com.falcons.proyecto_falcons.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "tutor")
public class Tutor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String telefono;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    @JsonIgnore
    private Usuario usuario;

    @OneToMany(mappedBy = "tutor")
    @JsonIgnore
    private List<Salon> salones;

    public Tutor() {
    }

    public Tutor(Long id, String telefono) {
        this.id = id;
        this.telefono = telefono;
    }

    public Long getId() {
        return id;
    }

    public String getTelefono() {
        return telefono;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public List<Salon> getSalones() {
        return salones;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setSalones(List<Salon> salones) {
        this.salones = salones;
    }
}