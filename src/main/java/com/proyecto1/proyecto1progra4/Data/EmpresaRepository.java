package com.proyecto1.proyecto1progra4.Data;

import com.proyecto1.proyecto1progra4.Logic.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
    List<Empresa> findByEstadoAprobacion(String estadoAprobacion);
}