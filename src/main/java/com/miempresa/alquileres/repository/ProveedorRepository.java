package com.miempresa.alquileres.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.miempresa.alquileres.model.Proveedor;

@Repository
public interface  ProveedorRepository extends JpaRepository<Proveedor, Long>{
    
}
