package com.sweet_temptation.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductoDTO {
    int id;
    String nombre;
    String descripcion;
    BigDecimal precio;
    int disponible;
    int unidades;
    LocalDateTime fechaRegistro;
    LocalDateTime fechaModificacion;
    int categoria;

    public ProductoDTO(int id, String nombre, String descripcion, BigDecimal precio,int disponible, int unidades, LocalDateTime fechaRegistro, LocalDateTime fechaModificacion, int categoria ){
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.disponible = disponible;
        this.unidades = unidades;
        this.fechaRegistro = fechaRegistro;
        this.fechaModificacion = fechaModificacion;
        this.categoria = categoria;
    }

    public ProductoDTO(int id, String nombre, String descripcion, BigDecimal precio, int disponible, int unidades, LocalDateTime fechaRegistro, LocalDateTime fechaModificacion) {
    }

    public int getId() { return id; }

    public String getNombre() { return nombre; }

    public String getDescripcion() { return descripcion; }

    public BigDecimal getPrecio() { return precio; }

    public int getDisponible() { return disponible; }

    public int getUnidades() { return unidades; }

    public LocalDateTime getFechaRegistro() { return fechaRegistro; }

    public LocalDateTime getFechaModificacion() { return fechaModificacion; }

    public int getCategoria() { return categoria; }


    public void setId(int id) { this.id = id; }

    public void setNombre(String nombre) { this.nombre = nombre; }

    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    public void setDisponible(int disponible) { this.disponible = disponible; }

    public void setUnidades(int unidades) { this.unidades = unidades; }

    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public void setFechaModificacion(LocalDateTime fechaModificacion) { this.fechaModificacion = fechaModificacion; }

    public void setCategoria(int categoria) { this.categoria = categoria; }
}
