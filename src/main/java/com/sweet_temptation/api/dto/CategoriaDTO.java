package com.sweet_temptation.api.dto;

public class CategoriaDTO {
    // 1. Campos que se envían al cliente WPF
    private Integer id;
    private String nombre;

    // 2. Constructor: Necesario para que el CategoriaService pueda crear el DTO.
    // Tu CategoriaService lo está usando explícitamente: new CategoriaDTO(categoria.getId(), categoria.getNombre());
    public CategoriaDTO(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    // 3. Constructor vacío: Necesario para que Spring y herramientas de serialización/deserialización
    // como Jackson/JSON puedan crear instancias.
    public CategoriaDTO() {
    }

    // 4. Getters y Setters: Necesarios para acceder a los datos.

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

    // Opcional: Si quieres un método toString() para debugging
    @Override
    public String toString() {
        return "CategoriaDTO{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
