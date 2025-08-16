package com.miempresa.alquileres.service;
import com.miempresa.alquileres.model.Proveedor;
import java.util.List;

public interface ProveedorService {
    Proveedor ObtenerPorId(Long Id);
    List<Proveedor> listarTodos();
}
