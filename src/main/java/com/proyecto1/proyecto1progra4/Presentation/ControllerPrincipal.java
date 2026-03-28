package com.proyecto1.proyecto1progra4.Presentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.proyecto1.proyecto1progra4.Logic.Service;
import org.springframework.web.bind.annotation.GetMapping;

@org.springframework.stereotype.Controller("ControllerPrincipal")
public class ControllerPrincipal {

    @Autowired
    private Service service;

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        model.addAttribute("puestos", service.top5PuestosPublicosRecientes());
        return "login/puestos";
    }
}