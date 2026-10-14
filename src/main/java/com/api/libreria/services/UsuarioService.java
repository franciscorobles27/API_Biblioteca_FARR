package com.api.libreria.services;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.api.libreria.models.UsuarioModel;
import com.api.libreria.repositories.*;
@Service public class UsuarioService {
    private final IUsuarioRepository usuarios;
    private final IPrestamoRepository prestamos;
    public UsuarioService(IUsuarioRepository usuarios,IPrestamoRepository prestamos){this.usuarios=usuarios;this.prestamos=prestamos;}
    public List<UsuarioModel> listar(){return usuarios.findAll();}
    public UsuarioModel obtener(Long id){return usuarios.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Usuario no encontrado"));}
    public UsuarioModel crear(UsuarioModel u){validar(u);if(usuarios.existsByCorreo(u.getCorreo()))throw new ResponseStatusException(HttpStatus.CONFLICT,"Correo ya registrado");return usuarios.save(u);}
    public UsuarioModel actualizar(Long id,UsuarioModel u){UsuarioModel actual=obtener(id);validar(u);
        if(!actual.getCorreo().equals(u.getCorreo())&&usuarios.existsByCorreo(u.getCorreo()))throw new ResponseStatusException(HttpStatus.CONFLICT,"Correo ya registrado");
        actual.setNombre(u.getNombre());actual.setApellidos(u.getApellidos());actual.setCorreo(u.getCorreo());
        if(u.getContrasena()!=null&&!u.getContrasena().isBlank())actual.setContrasena(u.getContrasena());
        actual.setRol(u.getRol());actual.setEstado(u.getEstado());actual.setFechaNacimiento(u.getFechaNacimiento());
        actual.setTelefono(u.getTelefono());actual.setDireccion(u.getDireccion());return usuarios.save(actual);}
    public void eliminar(Long id){obtener(id);if(prestamos.existsByUsuarioId(id))throw new ResponseStatusException(HttpStatus.CONFLICT,"Usuario con historial de prestamos");usuarios.deleteById(id);}
    private void validar(UsuarioModel u){if(u.getNombre()==null||u.getNombre().isBlank()||u.getCorreo()==null||!u.getCorreo().contains("@"))throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Nombre y correo valido son obligatorios");}
}
