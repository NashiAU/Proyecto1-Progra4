package com.proyecto1.proyecto1progra4.Presentation;

import com.proyecto1.proyecto1progra4.Data.EmpresaRepository;
import com.proyecto1.proyecto1progra4.Data.OferenteRepository;
import com.proyecto1.proyecto1progra4.Data.OferentehabilidadRepository;
import com.proyecto1.proyecto1progra4.Data.PuestoRepository;
import com.proyecto1.proyecto1progra4.Data.PuestocaracteristicaRepository;
import com.proyecto1.proyecto1progra4.Logic.*;
import com.proyecto1.proyecto1progra4.Security.UserDetailsImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

@Controller
public class ControllerEmpresa {

    @Autowired private Service service;
    @Autowired private EmpresaRepository empresaRepository;
    @Autowired private PuestoRepository puestoRepository;
    @Autowired private PuestocaracteristicaRepository puestoCaracteristicaRepository;
    @Autowired private OferenteRepository oferenteRepository;
    @Autowired private OferentehabilidadRepository habilidadRepository;

    // ── DASHBOARD ────────────────────────────────────────
    @GetMapping("/Empresa/dashboard")
    public String dashboard() {
        return "Empresa/dashboard";
    }

    // ── MIS PUESTOS ──────────────────────────────────────
    @GetMapping("/Empresa/puestos")
    public String puestos(@AuthenticationPrincipal UserDetailsImp userDetails, Model model) {
        Empresa empresa = empresaRepository.findByUsuario(userDetails.getUsuario())
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada"));
        model.addAttribute("puestos", service.puestosPorEmpresa(empresa.getId()));
        return "Empresa/puestos";
    }

    // ── DESACTIVAR PUESTO ────────────────────────────────
    @PostMapping("/Empresa/puestos/desactivar/{id}")
    public String desactivar(@PathVariable Long id) {
        service.desactivarPuesto(id);
        return "redirect:/Empresa/puestos";
    }

    // ── PUBLICAR PUESTO ──────────────────────────────────
    @GetMapping("/Empresa/publicar")
    public String formPublicar(Model model) {
        model.addAttribute("raices", service.caracteristicasRaiz());
        model.addAttribute("todas", service.todasLasCaracteristicas());
        return "Empresa/publicar";
    }

    @GetMapping("/Empresa/candidatos/cv/{id}")
    public ResponseEntity<Resource> verCvCandidato(@PathVariable Long id) throws IOException {
        Oferente oferente = oferenteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Oferente no encontrado"));

        Path path = Paths.get(oferente.getCvPath());
        Resource resource = new UrlResource(path.toUri());

        return ResponseEntity.ok()
                .header("Content-Disposition", "inline; filename=\"" + oferente.getCvNombreOriginal() + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }

    @PostMapping("/Empresa/publicar")
    public String publicarPuesto(@AuthenticationPrincipal UserDetailsImp userDetails,
                                 @RequestParam String descripcionGeneral,
                                 @RequestParam BigDecimal salarioOfrecido,
                                 @RequestParam String tipoPublicacion,
                                 @RequestParam(required = false) List<Long> caracteristicaIds,
                                 @RequestParam(required = false) List<Integer> niveles) {

        Empresa empresa = empresaRepository.findByUsuario(userDetails.getUsuario())
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada"));

        Puesto p = new Puesto();
        p.setEmpresa(empresa);
        p.setDescripcionGeneral(descripcionGeneral);
        p.setSalarioOfrecido(salarioOfrecido);
        p.setTipoPublicacion(tipoPublicacion);
        p.setActivo(true);
        p.setFechaRegistro(Instant.now());
        puestoRepository.save(p);

        // guardar características requeridas
        if (caracteristicaIds != null) {
            for (int i = 0; i < caracteristicaIds.size(); i++) {
                PuestocaracteristicaId pcId = new PuestocaracteristicaId();
                pcId.setPuestoId(p.getId());
                pcId.setCaracteristicaId(caracteristicaIds.get(i));

                Puestocaracteristica pc = new Puestocaracteristica();
                pc.setId(pcId);
                pc.setPuesto(p);

                Caracteristica c = new Caracteristica();
                c.setId(caracteristicaIds.get(i));
                pc.setCaracteristica(c);
                pc.setNivelDeseado(niveles.get(i));
                puestoCaracteristicaRepository.save(pc);
            }
        }

        return "redirect:/Empresa/puestos";
    }

    // ── BUSCAR CANDIDATOS ────────────────────────────────
    @GetMapping("/Empresa/candidatos/buscar")
    public String buscarCandidatos(@RequestParam Long puestoId, Model model) {
        Puesto puesto = puestoRepository.findById(puestoId)
                .orElseThrow(() -> new RuntimeException("Puesto no encontrado"));

        List<Puestocaracteristica> requisitos = puestoCaracteristicaRepository.findByPuesto(puesto);

        List<Oferente> todosOferentes = oferenteRepository.findByEstadoAprobacion("APROBADO");

        List<Map<String, Object>> candidatos = new ArrayList<>();

        for (Oferente o : todosOferentes) {
            List<Oferentehabilidad> habilidades = habilidadRepository.findByOferente(o);

            int cumplidos = 0;
            for (Puestocaracteristica req : requisitos) {
                for (Oferentehabilidad hab : habilidades) {
                    if (hab.getCaracteristica().getId().equals(req.getCaracteristica().getId())
                            && hab.getNivel() >= req.getNivelDeseado()) {
                        cumplidos++;
                        break;
                    }
                }
            }

            if (cumplidos > 0 || requisitos.isEmpty()) {
                double porcentaje = requisitos.isEmpty() ? 100.0
                        : (cumplidos * 100.0 / requisitos.size());

                Map<String, Object> entrada = new HashMap<>();
                entrada.put("oferente", o);
                entrada.put("cumplidos", cumplidos);
                entrada.put("total", requisitos.size());
                entrada.put("porcentaje", String.format("%.2f%%", porcentaje));
                candidatos.add(entrada);
            }
        }

        // ordenar de mayor a menor coincidencia
        candidatos.sort((a, b) -> Double.compare(
                Double.parseDouble(((String) b.get("porcentaje")).replace("%", "")),
                Double.parseDouble(((String) a.get("porcentaje")).replace("%", ""))
        ));

        model.addAttribute("puesto", puesto);
        model.addAttribute("candidatos", candidatos);
        return "Empresa/buscar";
    }

    // ── VER DETALLE DE CANDIDATO ─────────────────────────
    @GetMapping("/Empresa/candidatos/{id}")
    public String verCandidato(@PathVariable Long id, Model model) {
        Oferente oferente = oferenteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Oferente no encontrado"));
        List<Oferentehabilidad> habilidades = habilidadRepository.findByOferente(oferente);
        model.addAttribute("oferente", oferente);
        model.addAttribute("habilidades", habilidades);
        return "Empresa/detalles";
    }
}