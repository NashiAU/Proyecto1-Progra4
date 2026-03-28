package com.proyecto1.proyecto1progra4.Presentation;


import com.proyecto1.proyecto1progra4.Data.OferenteRepository;
import com.proyecto1.proyecto1progra4.Logic.Oferente;
import com.proyecto1.proyecto1progra4.Security.UserDetailsImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.Instant;

@Controller
public class ControllerOferente {

    @Autowired
    private OferenteRepository oferenteRepository;

    private static final String CARPETA_CVS = "src/main/resources/static/cvs/";

    @GetMapping("/Oferente/mi_cv")
    public String verCV(@AuthenticationPrincipal UserDetailsImp userDetails, Model model) {
        Oferente oferente = oferenteRepository.findByUsuario(userDetails.getUsuario())
                .orElse(null);

        if (oferente != null && oferente.getCvNombreOriginal() != null) {
            model.addAttribute("cvActual", oferente.getCvNombreOriginal());
        } else {
            model.addAttribute("cvActual", null);
        }

        return "Oferente/mi_cv";
    }

    @PostMapping("/Oferente/mi_cv/subir")
    public String subirCV(@AuthenticationPrincipal UserDetailsImp userDetails,
                          @RequestParam("archivo") MultipartFile archivo,
                          Model model) {

        if (archivo.isEmpty()) {
            model.addAttribute("error", "Seleccioná un archivo PDF.");
            return "Oferente/mi_cv";
        }

        if (!archivo.getContentType().equals("application/pdf")) {
            model.addAttribute("error", "Solo se permiten archivos PDF.");
            return "Oferente/mi_cv";
        }

        try {
            Oferente oferente = oferenteRepository.findByUsuario(userDetails.getUsuario())
                    .orElseThrow(() -> new RuntimeException("Oferente no encontrado"));

            if (oferente.getCvPath() != null) {
                File anterior = new File(oferente.getCvPath());
                if (anterior.exists()) anterior.delete();
            }

            String nombreArchivo = "oferente_" + oferente.getId() + ".pdf";
            Path destino = Paths.get(CARPETA_CVS + nombreArchivo);
            Files.createDirectories(destino.getParent());
            Files.copy(archivo.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);

            oferente.setCvPath(CARPETA_CVS + nombreArchivo);
            oferente.setCvNombreOriginal(archivo.getOriginalFilename());
            oferente.setCvMime(archivo.getContentType());
            oferente.setCvFechaSubida(Instant.now());
            oferenteRepository.save(oferente);

            model.addAttribute("mensaje", "CV subido correctamente.");
            model.addAttribute("cvActual", archivo.getOriginalFilename());

        } catch (IOException e) {
            model.addAttribute("error", "Error al guardar el archivo.");
        }

        return "Oferente/mi_cv";
    }

    @GetMapping("/Oferente/mi_cv/descargar")
    public org.springframework.http.ResponseEntity<org.springframework.core.io.Resource> descargarCV(
            @AuthenticationPrincipal UserDetailsImp userDetails) throws IOException {

        Oferente oferente = oferenteRepository.findByUsuario(userDetails.getUsuario())
                .orElseThrow(() -> new RuntimeException("Oferente no encontrado"));

        Path path = Paths.get(oferente.getCvPath());
        org.springframework.core.io.Resource resource =
                new org.springframework.core.io.UrlResource(path.toUri());

        return org.springframework.http.ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + oferente.getCvNombreOriginal() + "\"")
                .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                .body(resource);
    }
}