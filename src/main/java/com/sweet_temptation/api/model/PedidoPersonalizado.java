package com.sweet_temptation.api.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "PedidoPersonalizado")
public class PedidoPersonalizado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private int idPedido;

    @Column(length = 100)
    private String tamano;

    @Column(length = 100)
    private String saborBizcocho;

    @Column(length = 100)
    private String relleno;

    @Column(length = 100)
    private String cobertura;

    @Column(length = 100)
    private String especificaciones;

    private Date fechaCompra;
    private Date fechaSolicitud;

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

    public String getTamano() {
        return tamano;
    }

    public void setTamano(String tamano) {
        this.tamano = tamano;
    }

    public String getSaborBizcocho() {
        return saborBizcocho;
    }

    public void setSaborBizcocho(String saborBizcocho) {
        this.saborBizcocho = saborBizcocho;
    }

    public String getRelleno() {
        return relleno;
    }

    public void setRelleno(String relleno) {
        this.relleno = relleno;
    }

    public String getCobertura() {
        return cobertura;
    }

    public void setCobertura(String cobertura) {
        this.cobertura = cobertura;
    }

    public String getEspecificaciones() {
        return especificaciones;
    }

    public void setEspecificaciones(String especificaciones) {
        this.especificaciones = especificaciones;
    }

    public Date getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(Date fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public Date getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(Date fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }
}
