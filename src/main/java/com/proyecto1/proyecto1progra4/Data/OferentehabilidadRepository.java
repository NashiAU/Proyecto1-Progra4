package com.proyecto1.proyecto1progra4.Data;

import com.proyecto1.proyecto1progra4.Logic.Oferentehabilidad;
import com.proyecto1.proyecto1progra4.Logic.OferentehabilidadId;
import com.proyecto1.proyecto1progra4.Logic.Oferente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

    public interface OferentehabilidadRepository extends JpaRepository<Oferentehabilidad, OferentehabilidadId> {
        List<Oferentehabilidad> findByOferente(Oferente oferente);
        void deleteById(OferentehabilidadId id);
    }
