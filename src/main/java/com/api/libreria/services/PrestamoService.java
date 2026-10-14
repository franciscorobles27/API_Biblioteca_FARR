package com.api.libreria.services;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.api.libreria.models.*;
import com.api.libreria.repositories.*;
@Service public class PrestamoService {
    private final IPrestamoRepository prestamos;private final ILibroRepository libros;private final IUsuarioRepository usuarios;
    public PrestamoService(IPrestamoRepository p,ILibroRepository l,IUsuarioRepository u){prestamos=p;libros=l;usuarios=u;}
    public List<PrestamoModel> listar(){return prestamos.findAll();}
    public List<PrestamoModel> activos(){return prestamos.findByFechaDevolucionIsNull();}
    public PrestamoModel obtener(Long id){return prestamos.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Prestamo no encontrado"));}
    @Transactional public PrestamoModel prestar(Long isbn,Long usuarioId){
        LibroModel libro=libros.findById(isbn).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Libro no encontrado"));
        UsuarioModel usuario=usuarios.findById(usuarioId).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Usuario no encontrado"));
        if(!"ACTIVO".equalsIgnoreCase(usuario.getEstado()))throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Usuario inactivo");
        if(libro.getNumejemplares()<=0)throw new ResponseStatusException(HttpStatus.CONFLICT,"No hay ejemplares disponibles");
        libro.setNumejemplares(libro.getNumejemplares()-1);libros.save(libro);
        PrestamoModel prestamo=new PrestamoModel();prestamo.setLibro(libro);prestamo.setUsuario(usuario);prestamo.setFechaPrestamo(LocalDate.now());
        return prestamos.save(prestamo);
    }
    @Transactional public PrestamoModel devolver(Long id){PrestamoModel p=obtener(id);
        if(p.getFechaDevolucion()!=null)throw new ResponseStatusException(HttpStatus.CONFLICT,"Prestamo ya devuelto");
        p.setFechaDevolucion(LocalDate.now());LibroModel libro=p.getLibro();libro.setNumejemplares(libro.getNumejemplares()+1);libros.save(libro);return prestamos.save(p);}
}
