package com.sweet_temptation.api.dto;

import java.math.BigDecimal;

public class PagoRequestDTO {
    private String tipoPago;
    private BigDecimal montoPagado;
    private String detallesCuenta;

    public PagoRequestDTO() {
    }

    public PagoRequestDTO(String tipoPago, BigDecimal montoPagado, String detallesCuenta) {
        this.tipoPago = tipoPago;
        this.montoPagado = montoPagado;
        this.detallesCuenta = detallesCuenta;
    }

    public String getTipoPago() {
        return tipoPago;
    }

    public BigDecimal getMontoPagado() {
        return montoPagado;
    }

    public String getDetallesCuenta() {
        return detallesCuenta;
    }

    public void setTipoPago(String tipoPago) {
        this.tipoPago = tipoPago;
    }

    public void setMontoPagado(BigDecimal montoPagado) {
        this.montoPagado = montoPagado;
    }

    public void setDetallesCuenta(String detallesCuenta) {
        this.detallesCuenta = detallesCuenta;
    }

}
