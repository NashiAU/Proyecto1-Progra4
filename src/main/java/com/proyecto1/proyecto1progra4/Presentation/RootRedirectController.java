package com.proyecto1.proyecto1progra4.presentation;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@org.springframework.stereotype.Controller("RootRedirectController")
public class RootRedirectController {
    @GetMapping("/")
    public String redirect(Model model) {
        return "redirect:/presentation/prestamos/list";
        }
}
