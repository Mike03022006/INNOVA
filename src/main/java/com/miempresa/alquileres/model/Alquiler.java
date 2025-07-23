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
@Table(name = "alquileres")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Alquiler {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //** 
    @ManyToOne
    @JoinColumn(name = "equipo_id")
    private Equipo equipo;
    //**
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @Column(name = "fecha_inicio")
    private LocalDate fechaDeEntrega;

    @Column(name = "dias_alquiler")
    private int diasDeAlquiler;

    @Column(name = "costo_alquiler_cliente")
    private BigDecimal valor;

    @Column(name = "localizacion")
    private String localizacion;

    @Column(name = "estado")
    private Boolean estadoDeAlquiler;

    @Column(name = "numero_renovaciones")
    private String cantidadDeRenovaciones;
}
