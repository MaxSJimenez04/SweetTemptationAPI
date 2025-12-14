package com.sweet_temptation.api.validaciones;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Component
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

    public void validarLocalDate(LocalDate fechaInicio, LocalDate fechaFin){
        if(fechaInicio == null || fechaFin == null){
            throw new IllegalArgumentException("Las fechas no pueden estar vacías");
        }
        if(fechaInicio.isAfter(fechaFin)){
            throw new IllegalArgumentException("La fecha de inicio no puede ser antes de la fecha de fin");
        }

    }


    public int validarEstadoVenta(String estado){
        if(estado == null){
            throw new IllegalArgumentException("El estado no puede ser nulo");
        }

        String normalizado = estado.trim().toLowerCase();

        if (normalizado.isEmpty()) {
            return 0;
        }

        switch (normalizado) {
            case "completada":
            case "completado":
                return 3;
            case "cancelada":
            case "cancelado":
                return 4;
            case "pendiente":
                return 2;
            default:
                throw new IllegalArgumentException("Estado de venta inválido: " + estado);
        }
    }
}
