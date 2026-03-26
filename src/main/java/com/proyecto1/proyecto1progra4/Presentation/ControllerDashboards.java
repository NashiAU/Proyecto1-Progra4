package com.proyecto1.proyecto1progra4.Presentation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ControllerDashboards {
    @GetMapping("/Administrador/dashboard")
    public String admin() { return "Administrador/dashboard"; }

    @GetMapping("/Empresa/dashboard")
    public String empresa() { return "Empresa/dashboard"; }

    @GetMapping("/Oferente/dashboard")
    public String oferente() { return "Oferente/dashboard"; }
}