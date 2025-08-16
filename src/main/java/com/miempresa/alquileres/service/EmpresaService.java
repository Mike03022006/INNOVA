package com.miempresa.alquileres.service;
import java.util.List;

import com.miempresa.alquileres.model.Empresa;


public interface EmpresaService {
    Empresa ObtenerPorId(Long id);
    List<Empresa> listarTodas();
}
