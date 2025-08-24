package com.miempresa.alquileres.service;
import java.time.LocalDate;
import java.util.List;

import com.miempresa.alquileres.model.Alquiler;

public interface AlquilerService {
    List<Alquiler> obtenerTodos();
    List<Alquiler> obtenerAlquileresPorEmpresa(Long empresaId);
    void crearAlquiler(Long equipoId, Long empresaId, String proyecto, String ordenDeCliente, LocalDate fechaEntrega, int dias);
    void guardarAlquiler(Alquiler alquiler);
    void eliminarPorId(Long id);
}