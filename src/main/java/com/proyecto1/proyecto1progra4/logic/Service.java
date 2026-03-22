package com.proyecto1.proyecto1progra4.logic;

import com.proyecto1.proyecto1progra4.data.PrestamoRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@org.springframework.stereotype.Service
public class Service {
    @Autowired
    private PrestamoRepository prestamos;

    public List<Prestamo> prestamosAll() {
        return prestamos.findAll();
    }

    public List<Prestamo> prestamoSearch(String nombre) {
        return prestamos.findByNombre(nombre);
    }

    public void prestamosAdd(Prestamo prestamo) {
        if(prestamos.existsById(prestamo.getId())){
            throw new IllegalArgumentException("Prestamo ya existe");
        }
        prestamos.save(prestamo);
    }

    public Prestamo prestamoRead(String id) {
        return prestamos.findById(id).orElseThrow(() -> new IllegalArgumentException("Prestamo no existe"));
    }

    public void prestamoUpdate(Prestamo prestamo) {
        if(!prestamos.existsById(prestamo.getId())) {
            throw new IllegalArgumentException("Prestamo no existe");
        }
        prestamos.save(prestamo);
    }

    public void prestamoDelete(String id) {
        prestamos.deleteById(id);
    }

}