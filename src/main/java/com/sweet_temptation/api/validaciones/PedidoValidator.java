package com.sweet_temptation.api.validaciones;

import com.sweet_temptation.api.model.Pedido;
import org.springframework.stereotype.Component;
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
}
