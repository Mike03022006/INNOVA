package com.miempresa.alquileres.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

import com.miempresa.alquileres.model.Proveedor;
import com.miempresa.alquileres.repository.ProveedorRepository;
import com.miempresa.alquileres.service.ProveedorService;

@Service
public class ProveedorServicesImpl implements ProveedorService {
    @Autowired
    private ProveedorRepository proveedorRepository;
    @Override
    public Proveedor ObtenerPorId(Long Id){
        return proveedorRepository.findById(Id).orElse(null);
    }
    @Override
    public List<Proveedor> listarTodos(){
        return proveedorRepository.findAll();
    }
}
