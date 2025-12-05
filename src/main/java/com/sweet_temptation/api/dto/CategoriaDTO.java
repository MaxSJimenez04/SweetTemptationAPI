package com.sweet_temptation.api.dto;

public class CategoriaDTO {

    private Integer id;
    private String nombre;

    public CategoriaDTO(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public CategoriaDTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "CategoriaDTO{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
