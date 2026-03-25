package com.proyecto1.proyecto1progra4.Data;

import com.proyecto1.proyecto1progra4.Logic.Oferente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OferenteRepository extends JpaRepository<Oferente, Long> {
    List<Oferente> findByEstadoAprobacion(String estadoAprobacion);
    Optional<Oferente> findByIdentificacion(String identificacion);
}