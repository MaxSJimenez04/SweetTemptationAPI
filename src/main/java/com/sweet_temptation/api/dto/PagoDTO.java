package com.sweet_temptation.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PagoDTO {
    int id;
    BigDecimal total;
    LocalDateTime fechaPago;
    String tipoPago;
    String cuenta;
    int idPedido;

    public PagoDTO(int id, BigDecimal total, LocalDateTime fechaPago, String tipoPago, String cuenta, int idPedido){
        this.id = id;
        this.total = total;
        this.fechaPago = fechaPago;
        this.tipoPago = tipoPago;
        this.cuenta = cuenta;
        this.idPedido = idPedido;
    }

    public int getId() {
        return id;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public LocalDateTime getFechaPago() {
        return fechaPago;
    }

    public String getTipoPago() {
        return tipoPago;
    }

    public String getCuenta() {
        return cuenta;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public void setFechaPago(LocalDateTime fechaPago) {
        this.fechaPago = fechaPago;
    }

    public void setTipoPago(String tipoPago) {
        this.tipoPago = tipoPago;
    }

    public void setCuenta(String cuenta) {
        this.cuenta = cuenta;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }
}