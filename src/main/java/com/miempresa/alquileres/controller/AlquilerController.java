package com.miempresa.alquileres.controller;


import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.miempresa.alquileres.model.Alquiler;
import com.miempresa.alquileres.service.AlquilerService; 
import com.miempresa.alquileres.service.EmpresaService;
import com.miempresa.alquileres.service.EquipoService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/alquileres")
@RequiredArgsConstructor
public class AlquilerController {

    @Autowired
    private EquipoService equipoService;
    
    @Autowired
    private SpringTemplateEngine templateEngine;

    @Autowired
    private EmpresaService empresaService;

    private final AlquilerService alquilerService; 

    @GetMapping
    public String listarAlquileres(
        @RequestParam(required = false) String campo,
        @RequestParam(required = false) String operador,
        @RequestParam(required = false) String valor,
        Model model) {

        boolean hayFiltro = campo != null && !campo.isBlank()
                        && operador != null && !operador.isBlank()
                        && valor != null && !valor.isBlank();

        var lista = hayFiltro
                ? alquilerService.buscarUniversal(campo, operador, valor)
                : alquilerService.obtenerTodos();

        model.addAttribute("alquileres", lista);

        // Para mantener la selección en el formulario de búsqueda
        model.addAttribute("campo",    hayFiltro ? campo    : "proyecto");
        model.addAttribute("operador", hayFiltro ? operador : "=");
        model.addAttribute("valor",    hayFiltro ? valor    : "");

        return "alquileres";
    }


    @GetMapping("/empresa/{empresaId}")
    public String listarPorEmpresa(@PathVariable Long empresaId, Model model) {
        model.addAttribute("empresaId", empresaId);
        model.addAttribute("alquileres", alquilerService.obtenerAlquileresPorEmpresa(empresaId));
        return "alquileres_por_empresa";
    }

    @PostMapping("/crear")
    public String crearAlquiler(@ModelAttribute Alquiler alquiler) {
        alquilerService.guardarAlquiler(alquiler);
        return "redirect:/alquileres";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioCrearAlquiler(Model model) {
        model.addAttribute("alquiler", new Alquiler()); 
        model.addAttribute("equipos", equipoService.obtenerTodos()); 
        model.addAttribute("empresas", empresaService.listarTodas());
        return "crear_alquiler"; 
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarEquipo(@PathVariable Long id) {
        alquilerService.eliminarPorId(id);
        return "redirect:/alquileres"; 
    }
    @GetMapping("/buscar")
    public String buscarAlquileres(
        @RequestParam String campo,
        @RequestParam String operador,
        @RequestParam String valor) {

        String qs = "campo=" + URLEncoder.encode(campo, StandardCharsets.UTF_8)
                + "&operador=" + URLEncoder.encode(operador, StandardCharsets.UTF_8)
                + "&valor=" + URLEncoder.encode(valor, StandardCharsets.UTF_8);

        return "redirect:/alquileres?" + qs;
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Alquiler alquiler = alquilerService.obtenerPorId(id);
        if (alquiler == null) {
            return "redirect:/alquileres";
        }

        model.addAttribute("alquiler", alquiler);
        model.addAttribute("equipos", equipoService.obtenerTodos()); 
        model.addAttribute("empresas", empresaService.listarTodas());
        return "editar_alquiler";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarAlquiler(@PathVariable Long id, @ModelAttribute("alquiler") Alquiler alquilerForm) {
        Alquiler alquilerExistente = alquilerService.obtenerPorId(id);

        if (alquilerExistente == null) {
            return "redirect:/alquileres";
        }

        
        alquilerExistente.setProyecto(alquilerForm.getProyecto());
        alquilerExistente.setOrdenDeCliente(alquilerForm.getOrdenDeCliente());
        alquilerExistente.setReferencia(alquilerForm.getReferencia());
        alquilerExistente.setFechaDeEntrega(alquilerForm.getFechaDeEntrega());
        alquilerExistente.setDiasDeAlquiler(alquilerForm.getDiasDeAlquiler());
        alquilerExistente.setLocalizacion(alquilerForm.getLocalizacion());
        alquilerExistente.setUsuario(alquilerForm.getUsuario());
        alquilerExistente.setAdministrativo(alquilerForm.getAdministrativo());
        alquilerExistente.setEstado(alquilerForm.getEstado());
        alquilerExistente.setOrdenDeOdoo(alquilerForm.getOrdenDeOdoo());

        
        alquilerService.guardarAlquiler(alquilerExistente);

        return "redirect:/alquileres";
    }

    @GetMapping("/reporte")
public ResponseEntity<?> generarReporteAlquileres(
        @RequestParam(required = false) String campo,
        @RequestParam(required = false) String operador,
        @RequestParam(required = false) String valor) {

    boolean hayFiltro = campo != null && !campo.isBlank()
                     && operador != null && !operador.isBlank()
                     && valor != null && !valor.isBlank();

    var alquileres = hayFiltro
            ? alquilerService.buscarUniversal(campo, operador, valor)
            : alquilerService.obtenerTodos();

    var ctx = new org.thymeleaf.context.Context();
    ctx.setVariable("alquileres", alquileres);
    ctx.setVariable("titulo", "Reporte de Alquileres");
    ctx.setVariable("subtitulo", hayFiltro
        ? "Filtro: " + campo + " " + ("=".equals(operador) ? "igual a" : "contiene") + " " + valor
        : "Todos");

    final String html;
    try {
         html = templateEngine.process("reporte_alquileres", ctx);
    } catch (Exception te) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.TEXT_PLAIN)
                .body("Error procesando plantilla reporte_alquileres: " + te.getMessage());
    }

    try (var baos = new java.io.ByteArrayOutputStream()) {
        var builder = new com.openhtmltopdf.pdfboxout.PdfRendererBuilder();
        builder.useFastMode();
        builder.withHtmlContent(html, null);
        builder.toStream(baos);
        builder.run();

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.attachment()
                .filename("reporte_alquileres.pdf").build());
        return new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.OK);
    } catch (Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.TEXT_PLAIN)
                .body("Error generando PDF: " + ex.getMessage());
    }
}



}