package com.api.libreria.controllers;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.api.libreria.models.LibroModel;
import com.api.libreria.services.LibroService;
@RestController @RequestMapping("/libros")
public class LibroController {
    private final LibroService service;
    public LibroController(LibroService service){this.service=service;}
    @GetMapping public List<LibroModel> listar(){return service.obtenerLibros();}
    @GetMapping("/{isbn}") public LibroModel obtener(@PathVariable Long isbn){return service.obtenerPorId(isbn);}
    @GetMapping("/buscar") public List<LibroModel> buscar(@RequestParam String autor){return service.buscarPorAutor(autor);}
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public LibroModel guardar(@RequestBody LibroModel libro){return service.guardarLibro(libro);}
    @PutMapping("/{isbn}") public LibroModel actualizar(@PathVariable Long isbn,@RequestBody LibroModel libro){return service.actualizarLibro(libro,isbn);}
    @DeleteMapping("/{isbn}") @ResponseStatus(HttpStatus.NO_CONTENT) public void borrar(@PathVariable Long isbn){service.eliminarLibro(isbn);}
}
