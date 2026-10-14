package com.api.libreria.repositories;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.api.libreria.models.PrestamoModel;
public interface IPrestamoRepository extends JpaRepository<PrestamoModel,Long>{
    List<PrestamoModel> findByFechaDevolucionIsNull();
    boolean existsByLibroIsbn(Long isbn);
    boolean existsByUsuarioId(Long id);
}
