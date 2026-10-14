package com.api.libreria.services;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import com.api.libreria.models.LibroModel;
import com.api.libreria.repositories.ILibroRepository;
@Service
public class LibroService {
    private final ILibroRepository repository;
    private final com.api.libreria.repositories.IPrestamoRepository prestamos;
    public LibroService(ILibroRepository repository,com.api.libreria.repositories.IPrestamoRepository prestamos) {this.repository=repository;this.prestamos=prestamos;}
    public List<LibroModel> obtenerLibros(){return repository.findAll();}
    public LibroModel obtenerPorId(Long isbn){return repository.findById(isbn).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Libro no encontrado"));}
    public List<LibroModel> buscarPorAutor(String autor){return repository.findByAutorContainingIgnoreCase(autor);}
    public LibroModel guardarLibro(LibroModel libro){
        validar(libro);
        if(libro.getIsbn()==null || libro.getIsbn()<=0)throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"ISBN obligatorio y positivo");
        if(repository.existsById(libro.getIsbn()))throw new ResponseStatusException(HttpStatus.CONFLICT,"ISBN ya registrado");
        return repository.save(libro);
    }
    public LibroModel actualizarLibro(LibroModel request,Long isbn){
        LibroModel actual=obtenerPorId(isbn);validar(request);
        actual.setTitulo(request.getTitulo());actual.setAutor(request.getAutor());
        actual.setEditorial(request.getEditorial());actual.setAñoPublicacion(request.getAñoPublicacion());
        actual.setNumejemplares(request.getNumejemplares());return repository.save(actual);
    }
    public void eliminarLibro(Long isbn){obtenerPorId(isbn);if(prestamos.existsByLibroIsbn(isbn))throw new ResponseStatusException(HttpStatus.CONFLICT,"Libro con historial de prestamos");repository.deleteById(isbn);}
    private void validar(LibroModel libro){
        if(libro.getTitulo()==null||libro.getTitulo().isBlank()||libro.getAutor()==null||libro.getAutor().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Titulo y autor son obligatorios");
        if(libro.getNumejemplares()<0)throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Ejemplares no puede ser negativo");
    }
}
