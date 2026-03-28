package com.proyecto1.proyecto1progra4.Presentation;

import com.proyecto1.proyecto1progra4.Logic.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ControllerEmpresa {

    @Autowired
    private Service service;

    @GetMapping("/Empresa/dashboard")
    public String dashboard() {
        return "Empresa/dashboard";
    }

    @GetMapping("/Empresa/puestos")
    public String puestos() {
        return "Empresa/puestos";
    }

    @GetMapping("/Empresa/publicar")
    public String publicar() {
        return "Empresa/publicar";
    }
}