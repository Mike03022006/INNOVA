package com.miempresa.alquileres.service;
import java.util.List;

import com.miempresa.alquileres.model.Alquiler;

public interface AlquilerService {
    List<Alquiler> obtenerTodos();
    List<Alquiler> obtenerAlquileresPorEmpresa(Long empresaId);
}
