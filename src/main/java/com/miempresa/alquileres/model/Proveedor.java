package com.miempresa.alquileres.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "proveedores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Proveedor{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "nombre")
    private String nombreDeProveedor;

    @Column(name = "telefono")
    private String telefonDeProveedor;
    
    @Column(name = "contacto")
    private String contactoDeProveedor;

    @Column(name = "correo")
    private String correoDeProveedor;

    @Column(name = "direccion")
    private String direccionDeProveedor;

}
