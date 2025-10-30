package com.sweet_temptation.api.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "ImagenProducto")
public class ImagenProducto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(nullable = false)
    private int idProducto;

    @Column(nullable = false)
    private int idArchivo;

    private Date fechaRegistro;

    private Date fechaAsociacion;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getIdArchivo() {
        return idArchivo;
    }

    public void setIdArchivo(int idArchivo) {
        this.idArchivo = idArchivo;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Date getFechaAsociacion() {
        return fechaAsociacion;
    }

    public void setFechaAsociacion(Date fechaAsociacion) {
        this.fechaAsociacion = fechaAsociacion;
    }
}
