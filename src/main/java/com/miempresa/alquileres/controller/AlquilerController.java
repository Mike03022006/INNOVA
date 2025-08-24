package com.miempresa.alquileres.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    private EmpresaService empresaService;

    private final AlquilerService alquilerService; 

    @GetMapping
    public String listarAlquileres(Model model) {
        model.addAttribute("alquileres", alquilerService.obtenerTodos());
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
    
}