package com.falcons.proyecto_falcons.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "egresado")
public class Egresado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fechaEgreso;

    private String institucionDestino;

    private String carrera;

    private String observaciones;

    @OneToOne
    @JoinColumn(name = "estudiante_id", nullable = false, unique = true)
    @JsonIgnore
    private Estudiante estudiante;

    public Egresado() {
    }

    public Egresado(Long id, LocalDate fechaEgreso, String institucionDestino, String carrera, String observaciones) {
        this.id = id;
        this.fechaEgreso = fechaEgreso;
        this.institucionDestino = institucionDestino;
        this.carrera = carrera;
        this.observaciones = observaciones;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFechaEgreso() {
        return fechaEgreso;
    }

    public void setFechaEgreso(LocalDate fechaEgreso) {
        this.fechaEgreso = fechaEgreso;
    }

    public String getInstitucionDestino() {
        return institucionDestino;
    }

    public void setInstitucionDestino(String institucionDestino) {
        this.institucionDestino = institucionDestino;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }
}
