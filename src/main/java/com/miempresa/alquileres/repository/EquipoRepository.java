package com.miempresa.alquileres.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.miempresa.alquileres.model.Equipo;

@Repository
public interface EquipoRepository extends JpaRepository<Equipo, Long>{

    List<Equipo> findByProveedorNombreContaining(String nombreProveedor);

    List<Equipo> findBySerial(String serial);
    List<Equipo> findBySerialContaining(String serial);

    List<Equipo> findByPlaca(String placa);
    List<Equipo> findByPlacaContaining(String placa);

    List<Equipo> findByTipo(String tipo);
    List<Equipo> findByTipoContaining(String tipo);

    List<Equipo> findByCaracteristicas(String caracteristicas);
    List<Equipo> findByCaracteristicasContaining(String caracteristicas);

    List<Equipo> findByEstado(String estado);
    List<Equipo> findByEstadoContaining(String estado);

    List<Equipo> findByModalidadDeAdquisicion(String modalidad);
    List<Equipo> findByModalidadDeAdquisicionContaining(String modalidad);

    List<Equipo> findByCostoDeAdquisicion(BigDecimal costo);

    List<Equipo> findByFechaDeAdquisicion(LocalDate fecha);

    List<Equipo> findByRemisionDeProveedor(String remision);
    List<Equipo> findByRemisionDeProveedorContaining(String remision);

    List<Equipo> findByProveedorNombre(String nombreProveedor);
    List<Equipo> findByEmpresaId(long empresaID);

    List<Equipo> findByProveedorId(long proveedorId);
    @Query("""
        SELECT e FROM Equipo e
        WHERE e.id NOT IN (
            SELECT a.equipo.id FROM Alquiler a
            WHERE a.fechaDeDevolucion IS NULL OR a.fechaDeDevolucion >= CURRENT_DATE)""")
    List<Equipo> findEquiposDisponibles();
}


