package com.miempresa.alquileres.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;



import org.springframework.transaction.annotation.Transactional;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.miempresa.alquileres.model.Alquiler;
import com.miempresa.alquileres.model.Empresa;
import com.miempresa.alquileres.model.Equipo;
import com.miempresa.alquileres.repository.AlquilerRepository;
import com.miempresa.alquileres.repository.EmpresaRepository;
import com.miempresa.alquileres.repository.EquipoRepository;
import com.miempresa.alquileres.service.AlquilerService;
import com.miempresa.alquileres.service.EmpresaService;


@Service
public class AlquilerServiceImpl implements AlquilerService {

    @Autowired
    private EquipoRepository equipoRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private EmpresaService empresaService;


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

@Override
@Transactional
public void cargarAlquileresDesdeExcel(MultipartFile file) {
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
            if (filaVacia) continue;

            // ---------------- Serial (Equipo) ----------------
            String serial = getStringCellValue(row.getCell(0));
            if (serial == null) {
                System.out.println("Fila " + row.getRowNum() + ": serial vacío, se omite");
                continue;
            }

            Equipo equipo = equipoRepository.findBySerial(serial).stream().findFirst().orElse(null);
            if (equipo == null) {
                System.out.println("Fila " + row.getRowNum() + ": equipo no encontrado con serial " + serial);
                continue;
            }

            // ---------------- Cliente ID ----------------
            Long empresaId = getLongCellValue(row.getCell(1));
            Empresa empresa = (empresaId != null) ? empresaService.ObtenerPorId(empresaId) : null;
            if (empresa == null) {
                System.out.println("Fila " + row.getRowNum() + ": empresa no encontrada con ID " + empresaId);
                continue;
            }

            Alquiler alquiler = new Alquiler();
            alquiler.setEquipo(equipo);
            alquiler.setEmpresa(empresa);


            // ---------------- Orden cliente ----------------
            alquiler.setOrdenDeCliente(getStringCellValue(row.getCell(2)));

            // ---------------- Referencia Odoo ----------------
            alquiler.setReferencia(getStringCellValue(row.getCell(3)));

            // ---------------- Fecha entrega ----------------
            LocalDate fechaEntrega = getDateCellValue(row.getCell(4));
            alquiler.setFechaDeEntrega(fechaEntrega);

            // ---------------- Fecha devolución ----------------
            LocalDate fechaDevolucion = getDateCellValue(row.getCell(5));
            alquiler.setFechaDeDevolucion(fechaDevolucion);

            // ---------------- Días de alquiler ----------------
            if (fechaEntrega != null && fechaDevolucion != null) {
                long dias = ChronoUnit.DAYS.between(fechaEntrega, fechaDevolucion);
                alquiler.setDiasDeAlquiler((int) dias);
            }

            // ---------------- Valor ----------------
            Double valor = getNumericCellValue(row.getCell(6));
            if (valor != null) alquiler.setValor(BigDecimal.valueOf(valor));

            // ---------------- Localización ----------------
            alquiler.setLocalizacion(getStringCellValue(row.getCell(7)));

            // ---------------- Usuario ----------------
            alquiler.setUsuario(getStringCellValue(row.getCell(8)));

            // ---------------- Proyecto ----------------
            alquiler.setProyecto(getStringCellValue(row.getCell(9)));

            // ---------------- Orden Odoo ----------------
            alquiler.setOrdenDeOdoo(getStringCellValue(row.getCell(10)));

            // ---------------- Administrativo ----------------
            alquiler.setAdministrativo(getStringCellValue(row.getCell(11)));

            // ---------------- Estado ----------------
            String estado = getStringCellValue(row.getCell(12));
            alquiler.setEstado((estado != null && (estado.equalsIgnoreCase("1") || estado.equalsIgnoreCase("true"))));

            // ---------------- Número de renovaciones ----------------
            Long renovaciones = getLongCellValue(row.getCell(13));
            if (renovaciones != null) alquiler.setNumeroDeRenovaciones(renovaciones.intValue());

            // Guardar alquiler
            alquilerRepository.save(alquiler);
            System.out.println("Fila " + row.getRowNum() + " procesada: serial " + serial);
        }

    } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException("Error al procesar el archivo Excel de alquileres");
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
            return LocalDate.parse(
                cell.getStringCellValue().trim(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
            );
        }
    } catch (Exception e) {
        return null;
    }
    return null;
}


}
