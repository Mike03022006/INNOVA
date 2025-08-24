package com.miempresa.alquileres.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.miempresa.alquileres.model.Alquiler;
import com.miempresa.alquileres.model.Equipo;
import com.miempresa.alquileres.repository.AlquilerRepository;
import com.miempresa.alquileres.repository.EmpresaRepository;
import com.miempresa.alquileres.repository.EquipoRepository;
import com.miempresa.alquileres.service.AlquilerService;



@Service
public class AlquilerServiceImpl implements AlquilerService {

    @Autowired
    private EquipoRepository equipoRepository;

    @Autowired
    private EmpresaRepository empresaRepository;


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

    @Override
    public void crearAlquiler(Long equipoId, Long empresaId, String proyecto, String ordenDeCliente, LocalDate fechaEntrega, int dias) {
    Alquiler alquiler = new Alquiler();

    alquiler.setEquipo(equipoRepository.findById(equipoId)
        .orElseThrow(() -> new RuntimeException("Equipo no encontrado")));

    alquiler.setEmpresa(empresaRepository.findById(empresaId)
        .orElseThrow(() -> new RuntimeException("Empresa no encontrada")));

    alquiler.setProyecto(proyecto);
    alquiler.setOrdenDeCliente(ordenDeCliente);
    alquiler.setFechaDeEntrega(fechaEntrega);
    alquiler.setDiasDeAlquiler(dias);

    // Calcular valor del alquiler (ejemplo: 1% del costo diario del equipo)
    alquiler.setValor(calcularValorAlquiler(alquiler.getEquipo(), dias));

    alquilerRepository.save(alquiler);
}

private BigDecimal calcularValorAlquiler(Equipo equipo, int dias) {
    return equipo.getCostoDeAdquisicion()
                 .multiply(BigDecimal.valueOf(dias))
                 .multiply(BigDecimal.valueOf(0.01)); // 1% del costo diario
}

@Override
public void guardarAlquiler(Alquiler alquiler) {
    alquilerRepository.save(alquiler);
}

@Override
public void eliminarPorId(Long id) {
    alquilerRepository.deleteById(id);
}

}
