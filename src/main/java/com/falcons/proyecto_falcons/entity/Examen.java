package com.falcons.proyecto_falcons.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "examen")
public class Examen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String descripcion;
    private LocalDate fecha;
    private Integer duracionMinutos;
    private String tipo;

    @ManyToOne
    @JoinColumn(name = "curso_id", nullable = false)
    @JsonIgnore
    private Curso curso;

    @ManyToOne
    @JoinColumn(name = "docente_id", nullable = false)
    @JsonIgnore
    private Docente docente;

    // ❌ ELIMINADO (ya no existe relación con asistencia)
    // private List<Asistencia> asistencias;

    // ✅ ESTO SE QUEDA (NO AFECTA NADA)
    @OneToMany(mappedBy = "examen", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Calificacion> calificaciones = new ArrayList<>();

    public Examen() {}

    public Examen(Long id, String titulo, String descripcion, LocalDate fecha,
                  Integer duracionMinutos, String tipo, Curso curso, Docente docente) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.duracionMinutos = duracionMinutos;
        this.tipo = tipo;
        this.curso = curso;
        this.docente = docente;
    }

    public Long getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getDescripcion() { return descripcion; }
    public LocalDate getFecha() { return fecha; }
    public Integer getDuracionMinutos() { return duracionMinutos; }
    public String getTipo() { return tipo; }
    public Curso getCurso() { return curso; }
    public Docente getDocente() { return docente; }

    public List<Calificacion> getCalificaciones() { return calificaciones; }

    public void setId(Long id) { this.id = id; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public void setDuracionMinutos(Integer duracionMinutos) { this.duracionMinutos = duracionMinutos; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public void setCurso(Curso curso) { this.curso = curso; }
    public void setDocente(Docente docente) { this.docente = docente; }
    public void setCalificaciones(List<Calificacion> calificaciones) { this.calificaciones = calificaciones; }
}