package com.miempresa.alquileres.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.miempresa.alquileres.model.Alquiler;

@Repository
public interface AlquilerRepository extends JpaRepository<Alquiler, Long> {
    @Query("SELECT a.id FROM Alquiler a")
    List<Long> findAllIds();

    List<Alquiler> findByEmpresaId(Long empresaId);
}