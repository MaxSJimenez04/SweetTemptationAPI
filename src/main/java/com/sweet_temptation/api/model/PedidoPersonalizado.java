package com.sweet_temptation.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "PedidoPersonalizado")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PedidoPersonalizado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idPedido", nullable = false)
    private Pedido pedido;

    @Column(length = 50)
    private String tamano;

    @Column(length = 50)
    private String saborBizcocho;

    @Column(length = 50)
    private String relleno;

    @Column(length = 50)
    private String cobertura;

    @Column(length = 500)
    private String especificaciones;

    @Column(length = 255)
    private String imagen_Url;

    @Column(nullable = false)
    private LocalDateTime fechaSolicitud;

    @Column(length = 20)
    private String telefonoContacto;

    @Column(nullable = false)
    private Integer estado;

    public PedidoPersonalizado() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @com.fasterxml.jackson.annotation.JsonIgnore
    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
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

    public String getImagenUrl() {
        return imagen_Url;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagen_Url = imagenUrl;
    }

    public LocalDateTime getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(LocalDateTime fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public String getTelefonoContacto() {
        return telefonoContacto;
    }

    public void setTelefonoContacto(String telefonoContacto) {
        this.telefonoContacto = telefonoContacto;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }
}