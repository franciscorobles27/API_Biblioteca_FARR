package com.api.libreria.models;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "libros")
public class LibroModel {
    @Id
    

    private Long isbn;

    @Column
    private String titulo;

    @Column
    private String autor;

    @Column
    private String editorial;

    @Column
    private String añoPublicacion;
    
    @Column
    private int numejemplares;

    public Long getIsbn() {
        return isbn;
    }

    public void setIsbn(Long isbn) {
        this.isbn = isbn;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public String getAñoPublicacion() {
        return añoPublicacion;
    }

    public void setAñoPublicacion(String añoPublicacion) {
        this.añoPublicacion = añoPublicacion;
    }

    public int getNumejemplares() {
        return numejemplares;
    }

    public void setNumejemplares(int numejemplares) {
        this.numejemplares = numejemplares;
    }
    
}
