package com.sweet_temptation.api.model;

import jakarta.persistence.*;


@Entity
@Table(name = "ImagenCliente")
public class ImagenCliente {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id")
   private int id;

   @Column(nullable = false)
    private int idPedido;

   @Column(nullable = false)
   private int idArchivo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public int getIdArchivo() {
        return idArchivo;
    }

    public void setIdArchivo(int idArchivo) {
        this.idArchivo = idArchivo;
    }
}
