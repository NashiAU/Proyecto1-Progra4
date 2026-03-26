package com.proyecto1.proyecto1progra4.Presentation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ControllerPrincipal {
    @GetMapping({"/", "/home"})
    public String home() { return "login/puestos"; } // o el que tengas
}