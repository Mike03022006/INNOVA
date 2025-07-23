package com.miempresa.alquileres.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.miempresa.alquileres.model.Equipo;
import com.miempresa.alquileres.service.EquipoService;


@Controller
public class EquipoController {

    @Autowired
    private EquipoService equipoService;

    @GetMapping("/equipos")
    public String mostrarEquipos (Model model){
        List<Equipo> listaEquipos = equipoService.obtenerTodos();
        model.addAttribute("equipos", listaEquipos);

        return "equipos";
    }

        @GetMapping("/empresa/{id}/equipos")
    public String verEquiposPorEmpresa(@PathVariable Long id, Model model){
        //Empresa empresa = empresaService.ObtenerPorId(id);
        List<Equipo> equipos = equipoService.obtenerPorEmpresa(id);
        //model.addAttribute("empresa", empresa);
        model.addAttribute("equipos",equipos);
        return "equipos_por_empresa";
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
