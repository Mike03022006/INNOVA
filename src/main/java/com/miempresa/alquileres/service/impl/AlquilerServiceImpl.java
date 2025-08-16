package com.miempresa.alquileres.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.miempresa.alquileres.model.Alquiler;
import com.miempresa.alquileres.repository.AlquilerRepository;
import com.miempresa.alquileres.service.AlquilerService;



@Service
public class AlquilerServiceImpl implements AlquilerService {

    @Autowired
    private AlquilerRepository alquilerRepository;

    @Override
    public List<Alquiler> obtenerTodos() {
        return alquilerRepository.findAll();
    }

    @Override
    public List<Alquiler> obtenerAlquileresPorEmpresa(Long empresaId) {
        return alquilerRepository.findByEmpresaId(empresaId);
    }
}
