package com.sweet_temptation.api.dto;

import java.math.BigDecimal;

public class PagoResponseDTO {
    private int idPago;
    private String mensajeConfirmacion;
    private BigDecimal cambioDevuelto;
    private BigDecimal totalPagado;

    public PagoResponseDTO(int idPago, String mensajeConfirmacion, BigDecimal cambioDevuelto, BigDecimal totalPagado) {
        this.idPago = idPago;
        this.mensajeConfirmacion = mensajeConfirmacion;
        this.cambioDevuelto = cambioDevuelto;
        this.totalPagado = totalPagado;
    }

    public int getIdPago() {
        return idPago;
    }

    public String getMensajeConfirmacion() {
        return mensajeConfirmacion;
    }

    public BigDecimal getCambioDevuelto() {
        return cambioDevuelto;
    }

    public BigDecimal getTotalPagado() {
        return totalPagado;
    }

    public void setIdPago(int idPago) {
        this.idPago = idPago;
    }

    public void setMensajeConfirmacion(String mensajeConfirmacion) {
        this.mensajeConfirmacion = mensajeConfirmacion;
    }

    public void setCambioDevuelto(BigDecimal cambioDevuelto) {
        this.cambioDevuelto = cambioDevuelto;
    }

    public void setTotalPagado(BigDecimal totalPagado) {
        this.totalPagado = totalPagado;
    }
}
