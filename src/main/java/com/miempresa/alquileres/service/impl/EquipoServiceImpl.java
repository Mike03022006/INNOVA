package com.miempresa.alquileres.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.miempresa.alquileres.model.Equipo;
import com.miempresa.alquileres.repository.EquipoRepository;
import com.miempresa.alquileres.service.EquipoService;

@Service
public class EquipoServiceImpl implements EquipoService {

    @Autowired
    private EquipoRepository equipoRepository;

    @Override
    public List<Equipo> obtenerTodos() {
        return equipoRepository.findAll();
    }

    @Override
    public List<Equipo> obtenerPorEmpresa(Long empresaId){
        return equipoRepository.findByEmpresaId(empresaId);
    }

 @Override
public List<Equipo> buscarUniversal(String campo, String operador, String valor) {
    return switch (campo) {
        case "tipo" -> operador.equals("=") ? 
            equipoRepository.findByTipo(valor) :
            operador.equals("contiene") ? equipoRepository.findByTipoContaining(valor) : List.of();

        case "estado" -> equipoRepository.findByEstado(Boolean.valueOf(valor));

        case "referencia" -> operador.equals("=") ? 
            equipoRepository.findByReferencia(valor) :
            operador.equals("contiene") ? equipoRepository.findByReferenciaContaining(valor) : List.of();

        case "serial" -> operador.equals("=") ? 
            equipoRepository.findBySerial(valor) :
            operador.equals("contiene") ? equipoRepository.findBySerialContaining(valor) : List.of();

        case "placa" -> operador.equals("=") ? 
            equipoRepository.findByPlaca(valor) :
            operador.equals("contiene") ? equipoRepository.findByPlacaContaining(valor) : List.of();

        case "placaDeProveedor" -> operador.equals("=") ? 
            equipoRepository.findByPlacaDeProveedor(valor) :
            operador.equals("contiene") ? equipoRepository.findByPlacaDeProveedorContaining(valor) : List.of();

        case "fechaDeAdquisicion" -> {
            try {
                LocalDate fecha = LocalDate.parse(valor);
                yield equipoRepository.findByFechaDeAdquisicion(fecha);
            } catch (Exception e) {
                yield List.of();
            }
        }

        case "modalidadDeAdquisicion" -> operador.equals("=") ? 
            equipoRepository.findByModalidadDeAdquisicion(valor) :
            operador.equals("contiene") ? equipoRepository.findByModalidadDeAdquisicionContaining(valor) : List.of();

        case "serialRemisionDePoveedor" -> operador.equals("=") ? 
            equipoRepository.findBySerialRemisionDePoveedor(valor) :
            operador.equals("contiene") ? equipoRepository.findBySerialRemisionDePoveedorContaining(valor) : List.of();

        case "costoDeAlquilerDeProveedor" -> {
            try {
                BigDecimal costo = new BigDecimal(valor);
                yield equipoRepository.findByCostoDeAlquilerDeProveedor(costo);
            } catch (Exception e) {
                yield List.of();
            }
        }

        default -> List.of();
    };
}


}
