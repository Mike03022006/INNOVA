package com.miempresa.alquileres.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.miempresa.alquileres.model.Equipo;
import com.miempresa.alquileres.repository.EquipoRepository;
import com.miempresa.alquileres.repository.ProveedorRepository;
import com.miempresa.alquileres.service.EquipoService;
import com.miempresa.alquileres.service.ProveedorService;

@Service
public class EquipoServiceImpl implements EquipoService {

    @Autowired
    private ProveedorService proveedorService;

    private final EquipoRepository equipoRepository;

    public EquipoServiceImpl(EquipoRepository equipoRepository, ProveedorRepository proveedorRepository) {
        this.equipoRepository = equipoRepository;
    }

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

@Override
@Transactional
public void cargarDesdeExcel(MultipartFile file) {
    try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
        Sheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue; // Saltar encabezado
            // ---------------- Verificar si la fila está vacía ----------------
            boolean filaVacia = true;
            for (Cell cell : row) {
                String valor = getStringCellValue(cell);
                if (valor != null && !valor.isEmpty()) {
                    filaVacia = false;
                    break;
                }
            }
            if (filaVacia) continue; // Saltar fila vacía

            Equipo equipo = new Equipo();

                        // ---------------- Serial ----------------
            String serial = getStringCellValue(row.getCell(0));
            if (serial == null || !equipoRepository.findBySerial(serial).isEmpty()) {
                System.out.println("Fila " + row.getRowNum() + ": serial duplicado o vacío");
                continue;
            }
            equipo.setSerial(serial);

            // ---------------- Placa ----------------
            equipo.setPlaca(getStringCellValue(row.getCell(1)));

            // ---------------- Tipo ----------------
            equipo.setTipo(getStringCellValue(row.getCell(2)));

            // ---------------- Características ----------------
            equipo.setCaracteristicas(getStringCellValue(row.getCell(3)));

            // ---------------- Estado ----------------
            String estado = getStringCellValue(row.getCell(4));
            if (estado != null) {
                switch (estado.toUpperCase()) {
                    case "1", "ACTIVO" -> equipo.setEstado("1");
                    case "0", "INACTIVO" -> equipo.setEstado("0");
                    case "EN REPARACIÓN" -> equipo.setEstado("2");
                    default -> equipo.setEstado("0");
                }
            }

            // ---------------- Modalidad de adquisición ----------------
            equipo.setModalidadDeAdquisicion(getStringCellValue(row.getCell(5)));

            // ---------------- Costo de adquisición ----------------
            Double costo = getNumericCellValue(row.getCell(6));
            if (costo != null) equipo.setCostoDeAdquisicion(BigDecimal.valueOf(costo));

            // ---------------- Fecha de adquisición ----------------
            LocalDate fecha = getDateCellValue(row.getCell(7));
            equipo.setFechaDeAdquisicion(fecha);

            // ---------------- Remisión de proveedor ----------------
            equipo.setRemisionDeProveedor(getStringCellValue(row.getCell(8)));

            // ---------------- Proveedor ----------------
            Long proveedorId = getLongCellValue(row.getCell(9));
            if (proveedorId != null) equipo.setProveedor(proveedorService.ObtenerPorId(proveedorId));


            // Guardar equipo
            equipoRepository.save(equipo);
            System.out.println("Fila " + row.getRowNum() + " procesada: " + serial);
        }

    } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException("Error al procesar el archivo Excel");
    }
}

// ---------------- Funciones auxiliares ----------------
private String getStringCellValue(Cell cell) {
    if (cell == null) return null;
    return switch (cell.getCellType()) {
        case STRING -> cell.getStringCellValue().trim();
        case NUMERIC -> String.valueOf((long) cell.getNumericCellValue());
        case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
        default -> null;
    };
}

private Double getNumericCellValue(Cell cell) {
    if (cell == null) return null;
    return switch (cell.getCellType()) {
        case NUMERIC -> cell.getNumericCellValue();
        case STRING -> {
            try {
                yield Double.parseDouble(cell.getStringCellValue().trim());
            } catch (Exception e) {
                yield null;
            }
        }
        default -> null;
    };
}

private Long getLongCellValue(Cell cell) {
    Double value = getNumericCellValue(cell);
    return (value != null) ? value.longValue() : null;
}

private LocalDate getDateCellValue(Cell cell) {
    if (cell == null) return null;
    try {
        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            return cell.getLocalDateTimeCellValue().toLocalDate();
        } else if (cell.getCellType() == CellType.STRING) {
            return LocalDate.parse(cell.getStringCellValue().trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
    } catch (Exception e) {
        return null;
    }
    return null;
}


@Override
public void eliminarPorId(Long id) {
    equipoRepository.deleteById(id);
}

@Override
public List<Equipo> obtenerEquiposDisponibles() {
    return equipoRepository.findEquiposDisponibles();
}


}