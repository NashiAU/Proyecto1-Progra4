package com.proyecto1.proyecto1progra4.Presentation;

import com.proyecto1.proyecto1progra4.Logic.Caracteristica;
import com.proyecto1.proyecto1progra4.Logic.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ControllerAdmin {

    @Autowired
    private Service service;

    @GetMapping("/Administrador/dashboard")
    public String dashboard() {
        return "Administrador/dashboard";
    }

    @GetMapping("/Administrador/empresas")
    public String empresasPendientes(Model model) {
        model.addAttribute("empresas", service.empresasPendientes());
        return "Administrador/empresas";
    }

    @PostMapping("/Administrador/empresas/aprobar/{id}")
    public String aprobarEmpresa(@PathVariable Long id) {
        service.aprobarEmpresa(id);
        return "redirect:/Administrador/empresas";
    }

    @GetMapping("/Administrador/oferentes")
    public String oferentesPendientes(Model model) {
        model.addAttribute("oferentes", service.oferentesPendientes());
        return "Administrador/oferentes";
    }

    @PostMapping("/Administrador/oferentes/aprobar/{id}")
    public String aprobarOferente(@PathVariable Long id) {
        service.aprobarOferente(id);
        return "redirect:/Administrador/oferentes";
    }

    @GetMapping("/Administrador/caracteristicas")
    public String caracteristicas(Model model) {
        model.addAttribute("raices", service.caracteristicasRaiz());
        model.addAttribute("todas", service.todasLasCaracteristicas());
        return "Administrador/caracteristicas";
    }

    @PostMapping("/Administrador/caracteristicas/crear")
    public String crearCaracteristica(@RequestParam String nombre,
                                      @RequestParam(required = false) Long idPadre) {
        service.crearCaracteristica(nombre, idPadre);
        return "redirect:/Administrador/caracteristicas";
    }

    @GetMapping("/Administrador/reportes")
    public String reportes() {
        return "Administrador/reportes";
    }
}