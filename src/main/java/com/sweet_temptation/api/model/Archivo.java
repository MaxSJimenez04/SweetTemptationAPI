package com.sweet_temptation.api.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "Archivo", schema = "dbo")
public class Archivo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime fechaRegistro;

    @Column(length = 10)
    private String extension;

    @Lob
    @Column(name = "datos")
    private byte[] datos;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Archivo() {
    }

    public Archivo(int id, LocalDateTime fechaRegistro, String extension, byte[] datos) {
        this.id = id;
        this.fechaRegistro = fechaRegistro;
        this.extension = extension;
        this.datos = datos;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public byte[] getDatos() {
        return datos;
    }

    public void setDatos(byte[] datos) {
        this.datos = datos;
    }
}
