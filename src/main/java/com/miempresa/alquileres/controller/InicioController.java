package com.miempresa.alquileres.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InicioController{
    @GetMapping({"/","/inicio","/home"})
    public String mostrarInicio(){
        return "index";
    }
}
