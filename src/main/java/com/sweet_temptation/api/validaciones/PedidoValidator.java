package com.sweet_temptation.api.validaciones;

import com.sweet_temptation.api.model.Pedido;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Component
public class PedidoValidator {
    public void validarIDCliente(int idCliente){
        if (idCliente <= 0) {
            throw new IllegalArgumentException("ID del cliente inválido");
        }
    }

    public void validarIDPedido(int idPedido){
        if (idPedido <= 0) {
            throw new IllegalArgumentException("ID del pedido negativo");
        }
    }

    public void validarPedido(Pedido pedido){
        if(pedido == null){
            throw new NoSuchElementException("Pedido no encontrado");
        }
    }


    // ========== Estadisticas de ventas ==========

    public void validarRangoFecha(LocalDateTime inicio, LocalDateTime fin){
        if(inicio == null || fin == null){
            throw new IllegalArgumentException("Las fechas no pueden ser nulas");
        }
        if(fin.isBefore(inicio)){
                throw new IllegalArgumentException("La fecha de fin no puede ser antes de la fecha de inicio");
        }
    }

    public int validarEstadoVenta(String estadoDescripcion){
        if(estadoDescripcion == null){
            throw new IllegalArgumentException("El estado no puede ser nulo");
        }

        String normalizado = estadoDescripcion.trim().toLowerCase();

        switch (normalizado) {
            case "completada":
            case "completado":
                return 3;
            case "cancelada":
            case "cancelado":
                return 4;
            default:
                throw new IllegalArgumentException("Estado de venta inválido: " + estadoDescripcion);
        }
    }
}
