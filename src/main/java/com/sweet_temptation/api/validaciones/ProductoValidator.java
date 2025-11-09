package com.sweet_temptation.api.validaciones;

import com.sweet_temptation.api.dto.ProductoDTO;
import com.sweet_temptation.api.model.Producto;

import java.math.BigDecimal;

public class ProductoValidator {

    public void validarIDProducto(ProductoDTO producto){
        if(producto.getId() <= 0) throw new IllegalArgumentException("El ID del producto es invalido");
    }

    public void validarProducto(Producto producto){
        if(producto == null){
            throw new IllegalArgumentException("El producto es nulo");
        }
        if(producto.getNombre() == null || producto.getNombre().isBlank()){
            throw new IllegalArgumentException("Se necesita el nomnre");
        }
        if(producto.getPrecio() == null || producto.getPrecio().compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException("El precio es invalido");
        }
        if(producto.getUnidades() <= 0){
            throw new IllegalArgumentException("Las unidades del producto son invalidas");
        }
        /* TODO - pendiente
        if(producto.getCategoria() <= 0){
            throw new IllegalArgumentException("La categoria no coincide");
        }*/
    }

    // para registrar un nuevo producto
    public void validarProductoNuevo(ProductoDTO producto){
        if(producto == null){
            throw new IllegalArgumentException("El producto es nulo");
        }
        if(producto.getNombre() == null || producto.getNombre().isBlank()){
            throw new IllegalArgumentException("Falta el nombre del producto");
        }
        if(producto.getPrecio() == null || producto.getPrecio().compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException("El precio es invalido");
        }
        if(producto.getUnidades() < 0){
           throw new IllegalArgumentException("Las unidades del producto son invalidas");
        }
    }

    // TODO - Validaciones para modificar el producto
}
