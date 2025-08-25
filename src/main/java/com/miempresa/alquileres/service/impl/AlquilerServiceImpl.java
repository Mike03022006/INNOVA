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
    public Alquiler obtenerPorId(Long id) {
        return alquilerRepository.findById(id).orElse(null);
    }


    @Override
    public List<Alquiler> obtenerAlquileresPorEmpresa(Long empresaId) {
        return alquilerRepository.findByEmpresaId(empresaId);
    }

@Override
public List<Alquiler> buscarUniversal(String campo, String operador, String valor) {

    if (campo == null || campo.isBlank() || valor == null || valor.isBlank()) {
        return alquilerRepository.findAll();
    }

    String op = operador == null ? "=" : operador.trim().toLowerCase();
    String v  = valor.trim();

    if (campo.equalsIgnoreCase("serial") || campo.equalsIgnoreCase("placa")) {
        return alquilerRepository.buscarEnEquipo(campo.toLowerCase(), v);
    }

    if (campo.equalsIgnoreCase("empresa")) {
        try {
            // Si es número, busca por ID
            Long empresaId = Long.valueOf(v);
            return alquilerRepository.findByEmpresaId(empresaId);
        } catch (NumberFormatException e) {
            // Si no es número, busca por nombre
            return alquilerRepository.buscarEnEquipo(campo.toLowerCase(), v);
        }
    }

    return switch (campo) {
        case "ordenDeCliente" -> op.equals("=")
                ? alquilerRepository.findByordenDeCliente(v)
                : alquilerRepository.findByordenDeClienteContaining(v);

        case "referencia" -> op.equals("=")
                ? alquilerRepository.findByreferencia(v)
                : alquilerRepository.findByreferenciaContaining(v);

        case "fechaDeEntrega" -> {
            try {
                var fecha = java.time.LocalDate.parse(v);
                yield alquilerRepository.findByFechaDeEntrega(fecha);
            } catch (Exception e) {
                yield List.<Alquiler>of();
            }
        }

        case "fechaDeDevolucion" -> {
            try {
                var fecha = java.time.LocalDate.parse(v);
                yield alquilerRepository.findByFechaDeDevolucion(fecha);
            } catch (Exception e) {
                yield List.<Alquiler>of();
            }
        }

        case "diasDeAlquiler" -> {
            try {
                var dias = Integer.parseInt(v);
                yield alquilerRepository.findBydiasDeAlquiler(dias);
            } catch (Exception e) {
                yield List.<Alquiler>of();
            }
        }

        case "valor" -> {
            try {
                var monto = new java.math.BigDecimal(v);
                yield alquilerRepository.findByvalor(monto);
            } catch (Exception e) {
                yield List.<Alquiler>of();
            }
        }

        case "localizacion" -> op.equals("=")
                ? alquilerRepository.findBylocalizacion(v)
                : alquilerRepository.findBylocalizacionContaining(v);

        case "usuario" -> op.equals("=")
                ? alquilerRepository.findByusuario(v)
                : alquilerRepository.findByusuarioContaining(v);

        case "proyecto" -> op.equals("=")
                ? alquilerRepository.findByproyecto(v)
                : alquilerRepository.findByproyectoContaining(v);

        case "ordenDeOdoo" -> op.equals("=")
                ? alquilerRepository.findByordenDeOdoo(v)
                : alquilerRepository.findByordenDeOdooContaining(v);

        case "administrativo" -> op.equals("=")
                ? alquilerRepository.findByadministrativo(v)
                : alquilerRepository.findByadministrativoContaining(v);

        case "estado" -> {
            try {
                var bool = Boolean.parseBoolean(v);
                yield alquilerRepository.findByestado(bool);
            } catch (Exception e) {
                yield List.<Alquiler>of();
            }
        }

        case "renovaciones" -> {
            try {
                Integer numero = Integer.valueOf(v);
                yield alquilerRepository.findByNumeroDeRenovaciones(numero);
            } catch (NumberFormatException e) {
                yield List.of();
            }
        }

        default -> List.<Alquiler>of();
    };
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
