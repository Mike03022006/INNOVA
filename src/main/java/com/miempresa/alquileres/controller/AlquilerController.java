package com.miempresa.alquileres.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.miempresa.alquileres.service.AlquilerService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/alquileres")
@RequiredArgsConstructor
public class AlquilerController {

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
}