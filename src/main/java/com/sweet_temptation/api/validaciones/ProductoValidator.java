package com.sweet_temptation.api.validaciones;

import com.sweet_temptation.api.model.Producto;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Component
public class ProductoValidator {
    public void validarIDProducto(int id){
        if (id <= 0){
            throw new IllegalArgumentException("ID de producto inválido");
        }
    }

    public void validarProducto(Producto producto){
        if (producto == null){
            throw new NoSuchElementException("Pedido inválido");
        }
    }
}
