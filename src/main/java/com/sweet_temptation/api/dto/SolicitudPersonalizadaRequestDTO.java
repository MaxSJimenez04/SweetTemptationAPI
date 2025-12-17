package com.sweet_temptation.api.dto;

public class SolicitudPersonalizadaRequestDTO {

    private int idCliente;

    private String tamano;
    private String saborBizcocho;
    private String relleno;
    private String cobertura;
    private String especificaciones;
    private String imagenUrl;
    private String telefonoContacto;

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getTamano() { return tamano; }
    public void setTamano(String tamano) { this.tamano = tamano; }

    public String getSaborBizcocho() { return saborBizcocho; }
    public void setSaborBizcocho(String saborBizcocho) { this.saborBizcocho = saborBizcocho; }

    public String getRelleno() { return relleno; }
    public void setRelleno(String relleno) { this.relleno = relleno; }

    public String getCobertura() { return cobertura; }
    public void setCobertura(String cobertura) { this.cobertura = cobertura; }

    public String getEspecificaciones() { return especificaciones; }
    public void setEspecificaciones(String especificaciones) { this.especificaciones = especificaciones; }

    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }

    public String getTelefonoContacto() { return telefonoContacto; }
    public void setTelefonoContacto(String telefonoContacto) { this.telefonoContacto = telefonoContacto; }
}
