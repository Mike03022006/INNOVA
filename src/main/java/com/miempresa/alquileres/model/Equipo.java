package com.miempresa.alquileres.model;
import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name = "equipos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Equipo {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    //** 
    @ManyToOne
    @JoinColumn(name = "proveedor_id")
    private Proveedor proveedor;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;
    
    @Column(name = "tipo")
    private String tipo;

    @Column(name = "estado")
    private Boolean estado;

    @Column(name = "referencia")
    private String referencia;
    
    @Column(name = "serial")
    private String serial;

    @Column(name = "placa_interna")
    private String placa;

    @Column(name = "placa_proveedor")
    private String placaDeProveedor;

    @Column(name = "fecha_entrega_proveedor")
    private LocalDate fechaDeAdquisicion;

    @Column(name = "manera_adquisicion")
    private String modalidadDeAdquisicion;

    @Column(name = "remision_proveedor")
    private String serialRemisionDePoveedor;

    @Column(name = "costo_adquisicion")
    private BigDecimal costoDeAlquilerDeProveedor;
    
}
