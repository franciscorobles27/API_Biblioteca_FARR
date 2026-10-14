package com.api.libreria.models;
import java.time.LocalDate;
import jakarta.persistence.*;
@Entity @Table(name="prestamos")
public class PrestamoModel {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional=false) @JoinColumn(name="isbn") private LibroModel libro;
    @ManyToOne(optional=false) @JoinColumn(name="usuario_id") private UsuarioModel usuario;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucion;
    public Long getId(){return id;}public void setId(Long v){id=v;}
    public LibroModel getLibro(){return libro;}public void setLibro(LibroModel v){libro=v;}
    public UsuarioModel getUsuario(){return usuario;}public void setUsuario(UsuarioModel v){usuario=v;}
    public LocalDate getFechaPrestamo(){return fechaPrestamo;}public void setFechaPrestamo(LocalDate v){fechaPrestamo=v;}
    public LocalDate getFechaDevolucion(){return fechaDevolucion;}public void setFechaDevolucion(LocalDate v){fechaDevolucion=v;}
}
