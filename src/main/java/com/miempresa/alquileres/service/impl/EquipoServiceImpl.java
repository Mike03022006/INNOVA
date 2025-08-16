package com.miempresa.alquileres.service.impl;

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
    public void guardar(Equipo equipo){
        equipoRepository.save(equipo);
    }

    @Override
    public List<Equipo> obtenerPorEmpresa(Long empresaId){
        return equipoRepository.findByEmpresaId(empresaId);
    }

    @Override
    public List<Equipo> obtenerPorProveedor(Long proveedorId){
        return equipoRepository.findByProveedorId(proveedorId);
    }    
    
@Override
public List<Equipo> buscarUniversal(String campo, String operador, String valor) {
    // normaliza operador para evitar problemas de mayúsculas/espacios
    String op = operador == null ? "=" : operador.trim().toLowerCase();
    String v  = valor == null ? "" : valor.trim();

    return switch (campo) {
        case "tipo" -> op.equals("=")
                ? equipoRepository.findByTipo(v)
                : equipoRepository.findByTipoContaining(v);

        case "estado" -> op.equals("=")
                ? equipoRepository.findByEstado(v)
                : equipoRepository.findByEstadoContaining(v);

        case "serial" -> op.equals("=")
                ? equipoRepository.findBySerial(v)
                : equipoRepository.findBySerialContaining(v);

        case "placa" -> op.equals("=")
                ? equipoRepository.findByPlaca(v)
                : equipoRepository.findByPlacaContaining(v);

        case "caracteristicas" -> op.equals("=")
                ? equipoRepository.findByCaracteristicas(v)
                : equipoRepository.findByCaracteristicasContaining(v);

        case "modalidadDeAdquisicion" -> op.equals("=")
                ? equipoRepository.findByModalidadDeAdquisicion(v)
                : equipoRepository.findByModalidadDeAdquisicionContaining(v);

        // ✅ nombre correcto (antes estaba mal escrito)
        case "remisionDeProveedor" -> op.equals("=")
                ? equipoRepository.findByRemisionDeProveedor(v)
                : equipoRepository.findByRemisionDeProveedorContaining(v);

        case "fechaDeAdquisicion" -> {
            try {
                var fecha = java.time.LocalDate.parse(v);
                yield equipoRepository.findByFechaDeAdquisicion(fecha);
            } catch (Exception e) {
                yield java.util.List.of();
            }
        }

        // si quieres soportar exacto/contiene por proveedor:
        case "proveedorNombre" -> {
            if ("=".equals(op)) {
                yield equipoRepository.findByProveedorNombre(v);
            } else {
                // requiere el método containing en el repo (ver punto 2)
                try {
                    yield equipoRepository.findByProveedorNombreContaining(v);
                } catch (Exception e) {
                    yield equipoRepository.findByProveedorNombre(v); // fallback exacto
                }
            }
        }

        default -> java.util.List.of();
    };
}
}
