package com.sweet_temptation.api.dto;

public class EstadisticaProductoDTO {
    String categoria;
    String nombre;
    int ventas;

    public EstadisticaProductoDTO() {
    }

    public EstadisticaProductoDTO(String categoria, String nombre, int ventas) {
        this.categoria = categoria;
        this.nombre = nombre;
        this.ventas = ventas;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getVentas() {
        return ventas;
    }

    public void setVentas(int ventas) {
        this.ventas = ventas;
    }
}
