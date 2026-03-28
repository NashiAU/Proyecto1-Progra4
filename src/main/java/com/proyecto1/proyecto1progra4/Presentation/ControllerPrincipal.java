package com.proyecto1.proyecto1progra4.Presentation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.proyecto1.proyecto1progra4.Logic.*;
import org.springframework.web.bind.annotation.GetMapping;

@org.springframework.stereotype.Controller("ControllerPrincipal")
public class ControllerPrincipal {

    @Autowired
    private Service service;

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        List<Puesto> listaPuestos = service.top5PuestosPublicosRecientes();
        model.addAttribute("puestos", listaPuestos);
        model.addAttribute("requisitos", service.requisitosPorPuestos(listaPuestos));
        return "login/puestos";
    }
    @GetMapping("/puestos/buscar")
    public String buscarPuestos(@RequestParam(required = false) List<Long> caracteristicaIds,
                                Model model) {
        model.addAttribute("raices", service.caracteristicasRaiz());
        model.addAttribute("todas", service.todasLasCaracteristicas());

        if (caracteristicaIds != null && !caracteristicaIds.isEmpty()) {
            model.addAttribute("resultados", service.buscarPuestosPublicos(caracteristicaIds));
            model.addAttribute("buscado", true);
        } else {
            model.addAttribute("resultados", List.of());
            model.addAttribute("buscado", false);
        }

        return "login/buscar";
    }



    @GetMapping("/puestos/detalle/{id}")
    public String detallePuesto(@PathVariable Long id, Model model) {
        Puesto puesto = service.buscarPuestoPorId(id);
        model.addAttribute("puesto", puesto);
        model.addAttribute("requisitos", service.requisitosDelPuesto(id));
        return "login/detalle";
    }
}