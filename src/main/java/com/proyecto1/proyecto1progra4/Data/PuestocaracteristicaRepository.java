package com.proyecto1.proyecto1progra4.Data;

import com.proyecto1.proyecto1progra4.Logic.Puestocaracteristica;
import com.proyecto1.proyecto1progra4.Logic.PuestocaracteristicaId;
import com.proyecto1.proyecto1progra4.Logic.Puesto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PuestocaracteristicaRepository
        extends JpaRepository<Puestocaracteristica, PuestocaracteristicaId> {
    List<Puestocaracteristica> findByPuesto(Puesto puesto);
}
