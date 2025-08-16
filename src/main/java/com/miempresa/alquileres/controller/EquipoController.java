package com.miempresa.alquileres.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/equipos")
    public String mostrarEquipos (Model model){
        List<Equipo> listaEquipos = equipoService.obtenerTodos();
        model.addAttribute("equipos", listaEquipos);
        return "equipos";
    }


    @GetMapping("/equipos/crear")
    public String mostrarFormularioCrearEquipo(Model model){
        model.addAttribute("equipo",new Equipo());
        model.addAttribute("empresas", empresaService.listarTodas());
        model.addAttribute("proveedores", proveedorService.listarTodos());
        return "crear_equipo";
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
        @RequestParam String valor,
        Model model
    ) {
        List<Equipo> resultados = equipoService.buscarUniversal(campo, operador, valor);
        model.addAttribute("equipos", resultados);
        return "equipos";
    }
}
