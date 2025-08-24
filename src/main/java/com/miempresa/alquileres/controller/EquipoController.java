package com.miempresa.alquileres.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.miempresa.alquileres.model.Empresa;
import com.miempresa.alquileres.model.Equipo;
import com.miempresa.alquileres.service.EmpresaService;
import com.miempresa.alquileres.service.EquipoService;
import com.miempresa.alquileres.service.ProveedorService;

@Controller
public class EquipoController {

    @Autowired
    private EquipoService equipoService;

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private ProveedorService proveedorService;

    @Autowired
    private SpringTemplateEngine templateEngine;

@GetMapping("/equipos")
public String mostrarEquipos(
        @RequestParam(required = false) String campo,
        @RequestParam(required = false) String operador,
        @RequestParam(required = false) String valor,
        Model model) {

    boolean hayFiltro = campo != null && !campo.isBlank()
                     && operador != null && !operador.isBlank()
                     && valor != null && !valor.isBlank();

    var lista = hayFiltro
            ? equipoService.buscarUniversal(campo, operador, valor)
            : equipoService.obtenerTodos();

    model.addAttribute("equipos", lista);

    // valores "sticky" para el formulario
    model.addAttribute("campo",    hayFiltro ? campo    : "tipo");
    model.addAttribute("operador", hayFiltro ? operador : "=");
    model.addAttribute("valor",    hayFiltro ? valor    : "");

    return "equipos";
}



    @GetMapping("/equipos/crear")
    public String mostrarFormularioCrearEquipo(Model model){
        model.addAttribute("equipo", new Equipo());
        model.addAttribute("empresas", empresaService.listarTodas());
        model.addAttribute("proveedores", proveedorService.listarTodos());
        return "crear_equipo";  
    }

    @PostMapping("/equipos/cargar")
    public String cargarEquiposExcel(@RequestParam("file") MultipartFile file, Model model) {
    try {
        equipoService.cargarDesdeExcel(file);
        model.addAttribute("mensaje", "Archivo cargado correctamente");
    } catch (Exception e) {
        model.addAttribute("error", "Error al procesar el archivo: " + e.getMessage());
    }
    return "redirect:/equipos";
    }

    
    @PostMapping("/guardar")
    public String guardarEquipo(@ModelAttribute("equipo") Equipo equipo){
        equipoService.guardar(equipo);
        return "redirect:/equipos"; 
    }

        @GetMapping("/empresa/{id}/equipos")
    public String verEquiposPorEmpresa(@PathVariable Long id, Model model){
        Empresa empresa = empresaService.ObtenerPorId(id);
        List<Equipo> equipos = equipoService.obtenerPorEmpresa(id);
        model.addAttribute("empresa", empresa);
        model.addAttribute("equipos",equipos);
        return "equipos_por_empresa";
    }

        @GetMapping("/proveedor/{id}/equipos")
    public String verEquiposPorProveedor(@PathVariable Long id, Model model){
        var proveedor = proveedorService.ObtenerPorId(id);
        var equipos = equipoService.obtenerPorProveedor(id);
        model.addAttribute("proveedor", proveedor);
        model.addAttribute("equipos", equipos);
        return "equipos_por_proveedor";
    }


@GetMapping("/equipos/buscar")
public String buscar(
        @RequestParam String campo,
        @RequestParam String operador,
        @RequestParam String valor) {

    String qs = "campo=" + URLEncoder.encode(campo, StandardCharsets.UTF_8)
              + "&operador=" + URLEncoder.encode(operador, StandardCharsets.UTF_8)
              + "&valor=" + URLEncoder.encode(valor, StandardCharsets.UTF_8);

    return "redirect:/equipos?" + qs;
}


    @GetMapping("/equipos/eliminar/{id}")
    public String eliminarEquipo(@PathVariable Long id) {
        equipoService.eliminarPorId(id);
        return "redirect:/equipos"; 
    }

    
    @GetMapping("/equipos/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Equipo equipo = equipoService.obtenerTodos()
                                    .stream()
                                    .filter(e -> e.getId().equals(id))
                                    .findFirst()
                                    .orElse(null);

        if (equipo == null) {
            return "redirect:/equipos"; 
        }

        model.addAttribute("equipo", equipo);
        model.addAttribute("empresas", empresaService.listarTodas());
        model.addAttribute("proveedores", proveedorService.listarTodos());
        return "editar_equipo";
    }

    @PostMapping("/equipos/actualizar/{id}")
    public String actualizarEquipo(@PathVariable Long id, @ModelAttribute("equipo") Equipo equipo) {
        equipo.setId(id);
        equipoService.guardar(equipo); 
        return "redirect:/equipos";
    }

@GetMapping("/equipos/reporte")
public ResponseEntity<?> generarReporte(
        @RequestParam(required = false) String campo,
        @RequestParam(required = false) String operador,
        @RequestParam(required = false) String valor) {

    boolean hayFiltro = campo != null && !campo.isBlank()
                     && operador != null && !operador.isBlank()
                     && valor != null && !valor.isBlank();

    var equipos = hayFiltro
            ? equipoService.buscarUniversal(campo, operador, valor)
            : equipoService.obtenerTodos();

    // (si usas zwsWrap para romper textos largos, aplícalo aquí como ya lo tienes)

    var ctx = new org.thymeleaf.context.Context();
    ctx.setVariable("equipos", equipos);
    ctx.setVariable("titulo", "Reporte de Equipos");
    ctx.setVariable("subtitulo", hayFiltro
        ? "Filtro: " + campo + " " + ( "=".equals(operador) ? "igual a" : "contiene") + " " + valor
        : "Todos");

    final String html;
    try {
        html = templateEngine.process("reporte_equipos", ctx);
    } catch (Exception te) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.TEXT_PLAIN)
                .body("Error procesando plantilla reporte_equipos: " + te.getMessage());
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
                .filename("reporte_equipos.pdf").build());
        return new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.OK);
    } catch (Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.TEXT_PLAIN)
                .body("Error generando PDF: " + ex.getMessage());
    }
}

}