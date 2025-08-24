package com.miempresa.alquileres.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true, length=60)
    private String username;

    @Column(nullable=false, length=100)
    private String password; // BCrypt

    @Column(nullable=false, length=100)   // <-- NUEVO
    private String nombre;                // <-- NUEVO

    @Column(nullable=false)
    private boolean habilitado = true;

    @Column(nullable=false, length=150)
    private String correo;  // NUEVO

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "usuarios_roles",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "rol_id"))
    private Set<Rol> roles = new HashSet<>();

    public Usuario() {}

    // Constructor completo (opcional)
    public Usuario(String username, String password, String nombre, boolean habilitado) {
        this.username = username;
        this.password = password;
        this.nombre = nombre;
        this.habilitado = habilitado;
    }

    // getters / setters
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getNombre() { return nombre; }              // <-- NUEVO
    public void setNombre(String nombre) { this.nombre = nombre; } // <-- NUEVO

    public boolean isHabilitado() { return habilitado; }
    public void setHabilitado(boolean habilitado) { this.habilitado = habilitado; }
    public Set<Rol> getRoles() { return roles; }
    public void setRoles(Set<Rol> roles) { this.roles = roles; }
}
