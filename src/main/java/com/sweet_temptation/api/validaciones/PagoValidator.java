package com.sweet_temptation.api.validaciones;

import com.sweet_temptation.api.model.Pago;
import com.sweet_temptation.api.model.Pedido;
import org.springframework.stereotype.Component;
import java.util.NoSuchElementException;

@Component
public class PagoValidator {

    public void validarIDPedido(int idPedido){
        if (idPedido <= 0) {
            throw new IllegalArgumentException("ID del pedido inválido o negativo.");
        }
    }

    public void validarPago(Pago pago){
        if(pago == null){
            throw new NoSuchElementException("Pago no encontrado.");
        }
    }


    public void validarPedidoPendientePago(Pedido pedido){
        if(pedido == null){
            throw new NoSuchElementException("Pedido no encontrado.");
        }

        if (pedido.getEstado() == 3 || pedido.getActual() == false) {
            throw new IllegalArgumentException("El pedido con ID " + pedido.getId() + " ya ha sido pagado y completado.");
        }
    }

    public void validarDatosTarjeta(String detallesCuenta){
        if (detallesCuenta == null || detallesCuenta.trim().isEmpty()) {
            throw new IllegalArgumentException("Los detalles de la tarjeta (CLABE, número de cuenta, titular) son requeridos para el pago con tarjeta.");
        }
    }
}