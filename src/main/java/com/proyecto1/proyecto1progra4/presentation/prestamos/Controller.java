package com.proyecto1.proyecto1progra4.presentation.prestamos;

import com.proyecto1.proyecto1progra4.logic.Prestamo;
import com.proyecto1.proyecto1progra4.logic.Service;
import com.proyecto1.proyecto1progra4.logic.Usuario;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

@org.springframework.stereotype.Controller("prestamos")
@SessionAttributes({"prestamosSearch"})
@RequestMapping("/presentation/prestamos")
public class Controller {
    @Autowired
    private Service service;

    // Inicializa el objeto de búsqueda de préstamos en la sesión
    @ModelAttribute("prestamosSearch") public Prestamo prestamosSearch() {
        Prestamo prestamosSearch = new Prestamo();
        prestamosSearch.setNombre("");
        return prestamosSearch;
    }

    @GetMapping("/list")
    public String list(Model model, @ModelAttribute("prestamosSearch") Prestamo prestamosSearch) {
        model.addAttribute("prestamosList", service.prestamoSearch(prestamosSearch.getNombre()));
        return "presentation/prestamos/ViewList";
    }

    @PostMapping("/search")
    public String search(Model model, @ModelAttribute("prestamosSearch") Prestamo prestamosSearch) {
        model.addAttribute("prestamosList", service.prestamoSearch(prestamosSearch.getNombre()));
        return "presentation/prestamos/ViewList";
    }

    @GetMapping("/show")
    public String show(Model model,
                       @AuthenticationPrincipal(expression = "usuario") Usuario usuario) {
        Prestamo prestamo = new Prestamo();

        // Prellenar el campo de búsqueda con el ID del usuario autenticado
        // Solo para ilustrar el uso del usuario autenticado
        prestamo.setNombre(usuario.getId());

        model.addAttribute("prestamo", prestamo);

        // Indicar que no se está editando, sino creando un nuevo préstamo
        // El "action" del formulario se determinará en la vista según este atributo
        model.addAttribute("editing", false);

        return "presentation/prestamos/ViewEdit";
    }

    @PostMapping("/create")
    public String create(Model model, @ModelAttribute @Valid Prestamo prestamo, BindingResult result) {
        if (result.hasErrors()) {
            model.addAttribute("editing", false);
            return "presentation/prestamos/ViewEdit";
        }
        try {
            service.prestamosAdd(prestamo);
            return "redirect:/presentation/prestamos/list";
        } catch (Exception e) {
            result.addError(new FieldError("prestamo", "id", prestamo.getId(), false, null, null, "Prestamo ya existe"));
            model.addAttribute("editing", false);
            return "presentation/prestamos/ViewEdit";
        }
    }

    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable("id") String id) {
        try {
            model.addAttribute("prestamo", service.prestamoRead(id));

            // Indicar que se está editando un préstamo existente
            // El "action" del formulario se determinará en la vista según este atributo
            model.addAttribute("editing", true);

            return "presentation/prestamos/ViewEdit";
        } catch (Exception e) {
            return "redirect:/presentation/prestamos/list";
        }
    }

    @GetMapping("/delete/{id}")
    public String delete( Model model, @PathVariable("id") String id) {
        service.prestamoDelete(id);
        return "redirect:/presentation/prestamos/list";
    }

    @PostMapping("/update")
    public String update(Model model, @ModelAttribute @Valid Prestamo prestamo, BindingResult result) {
        if (result.hasErrors()) {
            model.addAttribute("editing", true);
            return "presentation/prestamos/ViewEdit";
        }
        try {
            service.prestamoUpdate(prestamo);
            return "redirect:/presentation/prestamos/list";
        } catch (Exception e) {
            result.addError(new FieldError("prestamo", "id", prestamo.getId(), false, null, null, "Prestamo no existe"));
            model.addAttribute("editing", true);
            return "presentation/prestamos/ViewEdit";
        }
    }
}
