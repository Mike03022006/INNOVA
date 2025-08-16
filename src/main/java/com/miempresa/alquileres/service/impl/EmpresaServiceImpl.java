package com.miempresa.alquileres.service.impl;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.miempresa.alquileres.model.Empresa;
import com.miempresa.alquileres.repository.EmpresaRepository;
import com.miempresa.alquileres.service.EmpresaService;

@Service
public class EmpresaServiceImpl implements EmpresaService {
    @Autowired
    private EmpresaRepository empresaRepository;
    @Override
    public Empresa ObtenerPorId(Long id) {
        return empresaRepository.findById(id).orElse(null);
    }
    @Override
    public List<Empresa> listarTodas() {
        return empresaRepository.findAll();
    }
}