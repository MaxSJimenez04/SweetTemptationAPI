package com.sweet_temptation.api.dto;

import java.math.BigDecimal;
import java.util.Date;

public class PedidoDTO {
    int id;
    Date fechaCompra;
    Boolean actual;
    BigDecimal total;
    int estado;
    Boolean personalizado;
    int idCliente;

    public PedidoDTO(int id, Date fechaCompra, Boolean actual, BigDecimal total, int estado, Boolean personalizado, int idCliente) {
        this.id = id;
        this.fechaCompra = fechaCompra;
        this.actual = actual;
        this.total = total;
        this.estado = estado;
        this.personalizado = personalizado;
        this.idCliente = idCliente;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(Date fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public Boolean getActual() {
        return actual;
    }

    public void setActual(Boolean actual) {
        this.actual = actual;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public Boolean getPersonalizado() {
        return personalizado;
    }

    public void setPersonalizado(Boolean personalizado) {
        this.personalizado = personalizado;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }
}
