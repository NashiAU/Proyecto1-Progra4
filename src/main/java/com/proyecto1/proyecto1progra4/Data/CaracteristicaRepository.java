package com.proyecto1.proyecto1progra4.Data;

import com.proyecto1.proyecto1progra4.Logic.Caracteristica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CaracteristicaRepository extends JpaRepository<Caracteristica, Long> {
    List<Caracteristica> findByIdPadre(Long idPadre);
}