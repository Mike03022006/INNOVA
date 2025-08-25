package com.miempresa.alquileres.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.miempresa.alquileres.model.Alquiler;

@Repository
public interface AlquilerRepository extends JpaRepository<Alquiler, Long> {
    @Query("SELECT a.id FROM Alquiler a")

    List<Long> findAllIds();

    List<Alquiler> findByEmpresaId(Long empresaId);

    List<Alquiler> findByordenDeCliente(String orden);
    List<Alquiler> findByordenDeClienteContaining(String orden);

    List<Alquiler> findByreferencia(String referencia);
    List<Alquiler> findByreferenciaContaining(String referencia);

    List<Alquiler> findByFechaDeEntrega(LocalDate fechaEntrega);

    List<Alquiler> findByFechaDeDevolucion(LocalDate fechaDevolucion);

    List<Alquiler> findBydiasDeAlquiler(int diasAlquiler);

    List<Alquiler> findByvalor(BigDecimal valor);

    List<Alquiler> findBylocalizacion(String localizacion);
    List<Alquiler> findBylocalizacionContaining(String localizacion);

    List<Alquiler> findByusuario(String usuario);
    List<Alquiler> findByusuarioContaining(String usuario);

    List<Alquiler> findByproyecto(String proyecto);
    List<Alquiler> findByproyectoContaining(String proyecto);

    List<Alquiler> findByordenDeOdoo(String ordenDeOdoo);
    List<Alquiler> findByordenDeOdooContaining(String ordenDeOdoo);

    List<Alquiler> findByadministrativo(String administrativo);
    List<Alquiler> findByadministrativoContaining(String administrativo);
    
    List<Alquiler> findByestado(Boolean estado);

    List<Alquiler> findByNumeroDeRenovaciones(Integer numeroRenovaciones);

@Query("""
    SELECT a FROM Alquiler a
    JOIN a.equipo e
    LEFT JOIN a.empresa emp
    WHERE
        (:campo = 'serial'  AND LOWER(e.serial) LIKE LOWER(CONCAT('%', :valor, '%')))
    OR  (:campo = 'placa'   AND LOWER(e.placa) LIKE LOWER(CONCAT('%', :valor, '%')))
    OR  (:campo = 'empresa' AND LOWER(emp.nombre) LIKE LOWER(CONCAT('%', :valor, '%')))
""")
List<Alquiler> buscarEnEquipo(@Param("campo") String campo, @Param("valor") String valor);



}