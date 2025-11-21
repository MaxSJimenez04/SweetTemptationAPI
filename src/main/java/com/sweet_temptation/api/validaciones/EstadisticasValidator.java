package com.sweet_temptation.api.validaciones;

import java.time.LocalDateTime;

public class EstadisticasValidator {
    // ========== Estadisticas de ventas ==========

    public void validarRangoFecha(LocalDateTime inicio, LocalDateTime fin){
        if(inicio == null || fin == null){
            throw new IllegalArgumentException("Las fechas no pueden ser nulas");
        }
        if(fin.isBefore(inicio)){
            throw new IllegalArgumentException("La fecha de fin no puede ser antes de la fecha de inicio");
        }
    }

    public int validarEstadoVenta(String estado){
        if(estado == null){
            throw new IllegalArgumentException("El estado no puede ser nulo");
        }

        String normalizado = estado.trim().toLowerCase();

        switch (normalizado) {
            case "completada":
            case "completado":
                return 3;
            case "cancelada":
            case "cancelado":
                return 4;
            default:
                throw new IllegalArgumentException("Estado de venta inválido: " + estado);
        }
    }
}
