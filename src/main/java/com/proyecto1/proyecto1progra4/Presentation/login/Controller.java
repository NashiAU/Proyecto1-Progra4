package com.proyecto1.proyecto1progra4.Presentation.login;

import org.springframework.web.bind.annotation.GetMapping;

@org.springframework.stereotype.Controller
public class Controller {
    @GetMapping("/login")
    public String login() {
        return "login/View";
    }

    @GetMapping("/notAuthorized")
    public String notAuthorized() {
        return "login/View"; // temporal para probar
    }
};
