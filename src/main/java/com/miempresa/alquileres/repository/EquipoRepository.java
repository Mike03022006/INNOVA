package com.miempresa.alquileres.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.miempresa.alquileres.model.Equipo;

@Repository
public interface EquipoRepository extends JpaRepository<Equipo, Long>{


    List<Equipo> findByTipo(String tipo);
    List<Equipo> findByTipoContaining(String tipo);

    List<Equipo> findByEstado(Boolean estado);

    List<Equipo> findByReferencia(String referencia);
    List<Equipo> findByReferenciaContaining(String referencia);

    List<Equipo> findBySerial(String serial);
    List<Equipo> findBySerialContaining(String serial);

    List<Equipo> findByPlaca(String placa);
    List<Equipo> findByPlacaContaining(String placa);

    List<Equipo> findByPlacaDeProveedor(String placaDeProveedor);
    List<Equipo> findByPlacaDeProveedorContaining(String placaDeProveedor);

    List<Equipo> findByFechaDeAdquisicion(LocalDate fecha);

    List<Equipo> findByModalidadDeAdquisicion(String modalidad);
    List<Equipo> findByModalidadDeAdquisicionContaining(String modalidad);

    List<Equipo> findBySerialRemisionDePoveedor(String remision);
    List<Equipo> findBySerialRemisionDePoveedorContaining(String remision);
    
    List<Equipo> findByCostoDeAlquilerDeProveedor(BigDecimal costo);

    List<Equipo> findByProveedorNombreDeProveedor(String nombreProveedor);
    List<Equipo> findByEmpresaId(long empresaID);

}


