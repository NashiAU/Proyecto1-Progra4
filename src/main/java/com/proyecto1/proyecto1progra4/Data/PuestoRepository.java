package com.proyecto1.proyecto1progra4.Data;

import com.proyecto1.proyecto1progra4.Logic.Puesto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PuestoRepository extends JpaRepository<Puesto, Long> {

    // Para la pagina de home: top 5 puestos públicos recientes
    List<Puesto> findTop5ByTipoPublicacionOrderByFechaRegistroDesc(String tipoPublicacion);

    // Para el dashboard de empresa: listar puestos de una empresa
    List<Puesto> findByEmpresaIdOrderByFechaRegistroDesc(Long empresaId);
}