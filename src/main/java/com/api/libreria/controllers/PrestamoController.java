package com.api.libreria.controllers;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.api.libreria.models.PrestamoModel;
import com.api.libreria.services.PrestamoService;
@RestController @RequestMapping("/prestamos") public class PrestamoController {
    private final PrestamoService service;public PrestamoController(PrestamoService s){service=s;}
    @GetMapping public List<PrestamoModel> listar(){return service.listar();}
    @GetMapping("/activos") public List<PrestamoModel> activos(){return service.activos();}
    @GetMapping("/{id}") public PrestamoModel obtener(@PathVariable Long id){return service.obtener(id);}
    public record SolicitudPrestamo(Long isbn,Long usuarioId){}
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public PrestamoModel crear(@RequestBody SolicitudPrestamo s){if(s.isbn()==null||s.usuarioId()==null)throw new org.springframework.web.server.ResponseStatusException(HttpStatus.BAD_REQUEST,"isbn y usuarioId son obligatorios");return service.prestar(s.isbn(),s.usuarioId());}
    @PutMapping("/{id}/devolver") public PrestamoModel devolver(@PathVariable Long id){return service.devolver(id);}
}
