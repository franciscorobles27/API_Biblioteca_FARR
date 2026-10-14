package com.api.libreria.models;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
@Entity @Table(name="usuarios")
public class UsuarioModel {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    private String nombre;
    private String apellidos;
    @Column(unique=true) private String correo;
    @JsonProperty(access=JsonProperty.Access.WRITE_ONLY) private String contrasena;
    private String rol;
    private String estado;
    private LocalDate fechaCreacion;
    private LocalDate fechaNacimiento;
    private String telefono;
    private String direccion;
    @PrePersist public void inicializar(){if(fechaCreacion==null)fechaCreacion=LocalDate.now();if(estado==null)estado="ACTIVO";if(rol==null)rol="SOCIO";}
    public Long getId(){return id;} public void setId(Long v){id=v;}
    public String getNombre(){return nombre;} public void setNombre(String v){nombre=v;}
    public String getApellidos(){return apellidos;} public void setApellidos(String v){apellidos=v;}
    public String getCorreo(){return correo;} public void setCorreo(String v){correo=v;}
    public String getContrasena(){return contrasena;} public void setContrasena(String v){contrasena=v;}
    public String getRol(){return rol;} public void setRol(String v){rol=v;}
    public String getEstado(){return estado;} public void setEstado(String v){estado=v;}
    public LocalDate getFechaCreacion(){return fechaCreacion;} public void setFechaCreacion(LocalDate v){fechaCreacion=v;}
    public LocalDate getFechaNacimiento(){return fechaNacimiento;} public void setFechaNacimiento(LocalDate v){fechaNacimiento=v;}
    public String getTelefono(){return telefono;} public void setTelefono(String v){telefono=v;}
    public String getDireccion(){return direccion;} public void setDireccion(String v){direccion=v;}
}
