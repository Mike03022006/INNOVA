package com.miempresa.alquileres.service;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.miempresa.alquileres.model.Equipo;

public interface EquipoService {
    List<Equipo> obtenerTodos();
    List<Equipo> obtenerPorEmpresa(Long empresaId);
    List<Equipo> buscarUniversal(String campo, String operador, String valor);
    List<Equipo> obtenerPorProveedor(Long proveedorId);
    void guardar(Equipo equipo);
    void cargarDesdeExcel(MultipartFile file) throws Exception;
    void eliminarPorId(Long id);
    List<Equipo> obtenerEquiposDisponibles();
}