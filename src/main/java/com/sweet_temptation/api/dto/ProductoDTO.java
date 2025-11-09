package com.sweet_temptation.api.dto;

import org.springframework.cglib.core.Local;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

public class ProductoDTO {
    int id;
    String nombre;
    String descripcion;
    double precio;
    int dispobible;
    int unidades;
    LocalDateTime fechaRegistro;
    LocalDateTime fechaModificacion;
    int categoria;

    public ProductoDTO(int id, String nombre, String descripcion, double precio,int dispobible, int unidades, LocalDateTime fechaRegistro, LocalDateTime fechaModificacion, int categoria ){
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.dispobible = dispobible;
        this.unidades = unidades;
        this.fechaRegistro = fechaRegistro;
        this.fechaModificacion = fechaModificacion;
        this.categoria = categoria;
    }

    public int getId() { return id; }

    public String getNombre() { return nombre; }

    public String getDescripcion() { return descripcion; }

    public double getPrecio() { return precio; }

    public int getDispobible() { return dispobible; }

    public int getUnidades() { return unidades; }

    public LocalDateTime getFechaRegistro() { return fechaRegistro; }

    public LocalDateTime getFechaModificacion() { return fechaModificacion; }

    public int getCategoria() { return categoria; }


    public void setId(int id) { this.id = id; }

    public void setNombre(String nombre) { this.nombre = nombre; }

    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public void setPrecio(double precio) { this.precio = precio; }

    public void setDispobible(int dispobible) { this.dispobible = dispobible; }

    public void setUnidades(int unidades) { this.unidades = unidades; }

    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public void setFechaModificacion(LocalDateTime fechaModificacion) { this.fechaModificacion = fechaModificacion; }

    public void setCategoria(int categoria) { this.categoria = categoria; }
}
