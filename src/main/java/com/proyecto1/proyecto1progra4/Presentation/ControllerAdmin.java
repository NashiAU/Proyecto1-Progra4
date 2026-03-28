package com.proyecto1.proyecto1progra4.Presentation;



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

    @GetMapping("/Administrador/oferentes")
    public String oferentesPendientes(Model model) {
        model.addAttribute("oferentes", service.oferentesPendientes());
        return "Administrador/oferentes";
    }

    @GetMapping("/Administrador/caracteristicas")
    public String caracteristicas(Model model) {
        model.addAttribute("raices", service.caracteristicasRaiz());
        return "Administrador/caracteristicas";
    }

    @GetMapping("/Administrador/reportes")
    public String reportes() {
        return "Administrador/reportes";
    }
}