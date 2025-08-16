package com.miempresa.alquileres.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; 
import org.springframework.web.bind.annotation.GetMapping;

import com.miempresa.alquileres.model.Empresa;
import com.miempresa.alquileres.service.EmpresaService;

@Controller
public class EmpresaController {
    @Autowired
    private EmpresaService empresaService;
    @GetMapping("/menu_empresas")
    public String listarEmpresas(Model model){
        List<Empresa> empresas = empresaService.listarTodas(); 
        model.addAttribute("empresas", empresas);         
        return "menu_empresas";     
    }

}