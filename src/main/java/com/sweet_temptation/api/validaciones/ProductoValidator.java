package com.sweet_temptation.api.validaciones;

import com.sweet_temptation.api.dto.ProductoDTO;
import com.sweet_temptation.api.model.Producto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ProductoValidator {

    public void validarIDProducto(int idProducto){
        if(idProducto <= 0) throw new IllegalArgumentException("El ID del producto es invalido");
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
        if(producto.getUnidades() <= 0) {
            throw new IllegalArgumentException("Las unidades del producto son invalidas");
        }
        if(producto.getIdCategoria() <= 0){
            throw new IllegalArgumentException("La categoria no coincide");
        }
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

    // checar si puede haber campos opcionales
    public void validarProductoModificado(ProductoDTO producto){
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

    public void validarPrecio(BigDecimal precio){
        if(precio == null){
            throw new IllegalArgumentException("El precio es invalido");
        }
    }

    public void validarStock(int unidades){
        if(unidades < 0){
            throw new IllegalArgumentException("El stock no puede ser menor a 0");
        }
    }
}
