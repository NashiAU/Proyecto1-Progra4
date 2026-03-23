package com.proyecto1.proyecto1progra4.data;

import com.proyecto1.proyecto1progra4.logic.Prestamo;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PrestamoRepository {
    @Query("select p from Prestamo p where p.nombre like %?1%")
    public List<Prestamo> findByNombre(String nombre);
}
