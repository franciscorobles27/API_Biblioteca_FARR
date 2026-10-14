package com.api.libreria.controllers;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.api.libreria.models.UsuarioModel;
import com.api.libreria.services.UsuarioService;
@RestController @RequestMapping("/usuarios") public class UsuarioController {
    private final UsuarioService service;public UsuarioController(UsuarioService s){service=s;}
    @GetMapping public List<UsuarioModel> listar(){return service.listar();}
    @GetMapping("/{id}") public UsuarioModel obtener(@PathVariable Long id){return service.obtener(id);}
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public UsuarioModel crear(@RequestBody UsuarioModel u){return service.crear(u);}
    @PutMapping("/{id}") public UsuarioModel actualizar(@PathVariable Long id,@RequestBody UsuarioModel u){return service.actualizar(id,u);}
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void borrar(@PathVariable Long id){service.eliminar(id);}
}
