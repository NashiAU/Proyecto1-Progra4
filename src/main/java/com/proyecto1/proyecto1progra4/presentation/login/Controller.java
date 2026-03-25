package com.proyecto1.proyecto1progra4.Presentation.Login;

import org.springframework.web.bind.annotation.GetMapping;

@org.springframework.stereotype.Controller
public class Controller {
    @GetMapping("/login")
    public String login() {
        return "presentation/login/View";
    }

    @GetMapping("/notAuthorized")
    public String notAuthorized() {
        return "presentation/login/ViewNotAuthorized";
    }
}
