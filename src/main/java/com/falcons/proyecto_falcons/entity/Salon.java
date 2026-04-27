package com.falcons.proyecto_falcons.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "salon")
public class Salon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private Integer capacidad;

    @ManyToOne
    @JoinColumn(name = "tutor_id")
    @JsonIgnore
    private Tutor tutor;

    @ManyToOne
    @JoinColumn(name = "local_id")
    @JsonIgnore
    private Local local;

    @OneToMany(mappedBy = "salon", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Estudiante> estudiantes;

    @OneToMany(mappedBy = "salon", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Horario> horarios;

    public Salon() {
    }

    public Salon(Long id, String nombre, Integer capacidad) {
        this.id = id;
        this.nombre = nombre;
        this.capacidad = capacidad;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public Integer getCapacidad() {
        return capacidad;
    }

    public Tutor getTutor() {
        return tutor;
    }

    public Local getLocal() {
        return local;
    }

    public List<Estudiante> getEstudiantes() {
        return estudiantes;
    }

    public List<Horario> getHorarios() {
        return horarios;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }

    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
    }

    public void setLocal(Local local) {
        this.local = local;
    }

    public void setEstudiantes(List<Estudiante> estudiantes) {
        this.estudiantes = estudiantes;
    }

    public void setHorarios(List<Horario> horarios) {
        this.horarios = horarios;
    }
}