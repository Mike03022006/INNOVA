package com.miempresa.alquileres.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.miempresa.alquileres.model.Proveedor;
import com.miempresa.alquileres.service.ProveedorService; 




@Controller
public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService;

    @GetMapping("/menu_proveedores")
    public String verListaProveedores(Model model){
        List<Proveedor> proveedores = proveedorService.listarTodos();
        model.addAttribute("proveedores", proveedores);
        return "menu_proveedores";
    }

}
