package com.miempresa.alquileres.model;
import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    //** 
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "equipo_id")
    private Equipo equipo;
    //** 
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cliente_id")
    private Empresa empresa;
    
    @Column(name = "orden_cliente")
    private String ordenDeCliente;
        
    @Column(name = "referencia_odoo")
    private String referencia;

    @Column(name = "fecha_entrega")
    private LocalDate fechaDeEntrega;

    @Column(name = "fecha_devolucion")
    private LocalDate fechaDeDevolucion;

    @Column(name = "dias_alquiler")
    private int diasDeAlquiler;

    @Column(name = "valor")
    private BigDecimal valor;

    @Column(name = "localizacion")
    private String localizacion;

    @Column(name = "usuario")
    private String usuario;

    @Column(name = "proyecto")
    private String proyecto;

    @Column(name = "orden_odoo")
    private String ordenDeOdoo;

    @Column(name = "administrativo")
    private String administrativo;

    @Column(name = "estado")
    private Boolean estado; 

    @Column(name = "numero_renovaciones")
    private Integer numeroDeRenovaciones;

}
