package com.miempresa.alquileres.service;
import java.util.List;

import com.miempresa.alquileres.model.Equipo;



public interface EquipoService {
    List<Equipo> obtenerTodos();
    List<Equipo> obtenerPorEmpresa(Long empresaId);
    List<Equipo> buscarUniversal(String campo, String operador, String valor);
}


