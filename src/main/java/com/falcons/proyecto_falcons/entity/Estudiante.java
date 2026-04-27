package com.falcons.proyecto_falcons.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "estudiante")
public class Estudiante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fechaNacimiento;
    private String telefono;
    private String numeroApoderado;
    private String direccion;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinColumn(name = "salon_id")
    private Salon salon;

    @OneToMany(mappedBy = "estudiante")
    @JsonIgnore
    private List<Matricula> matriculas;

    @OneToMany(mappedBy = "estudiante")
    @JsonIgnore
    private List<Pago> pagos;

    @OneToMany(mappedBy = "estudiante")
    @JsonIgnore
    private List<Calificacion> calificaciones;

    @OneToMany(mappedBy = "estudiante")
    @JsonIgnore
    private List<Asistencia> asistencias;

    public Estudiante() {}

    public Estudiante(Long id, LocalDate fechaNacimiento, String telefono,
                      String numeroApoderado, String direccion) {
        this.id = id;
        this.fechaNacimiento = fechaNacimiento;
        this.telefono = telefono;
        this.numeroApoderado = numeroApoderado;
        this.direccion = direccion;
    }

    public Long getId() { return id; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public String getTelefono() { return telefono; }
    public String getNumeroApoderado() { return numeroApoderado; }
    public String getDireccion() { return direccion; }
    public Usuario getUsuario() { return usuario; }
    public Salon getSalon() { return salon; }
    public List<Matricula> getMatriculas() { return matriculas; }
    public List<Pago> getPagos() { return pagos; }
    public List<Calificacion> getCalificaciones() { return calificaciones; }
    public List<Asistencia> getAsistencias() { return asistencias; }

    public void setId(Long id) { this.id = id; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setNumeroApoderado(String numeroApoderado) { this.numeroApoderado = numeroApoderado; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public void setSalon(Salon salon) { this.salon = salon; }
    public void setMatriculas(List<Matricula> matriculas) { this.matriculas = matriculas; }
    public void setPagos(List<Pago> pagos) { this.pagos = pagos; }
    public void setCalificaciones(List<Calificacion> calificaciones) { this.calificaciones = calificaciones; }
    public void setAsistencias(List<Asistencia> asistencias) { this.asistencias = asistencias; }
}