package com.sweet_temptation.api.validaciones;

import com.sweet_temptation.api.dto.ProductoDTO;
import com.sweet_temptation.api.model.Producto;
import com.sweet_temptation.api.repository.ProductoRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ProductoValidator {

    private final ProductoRepository productoRepository;

    public ProductoValidator(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public void validarIDProducto(int idProducto){
        if(idProducto <= 0)
            throw new IllegalArgumentException("El ID del producto es inválido");
    }

    public void validarProducto(Producto producto){
        if(producto == null)
            throw new IllegalArgumentException("El producto es nulo");

        if(producto.getNombre() == null || producto.getNombre().isBlank())
            throw new IllegalArgumentException("Se necesita el nombre");

        if(producto.getPrecio() == null || producto.getPrecio().compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("El precio es inválido");

        if(producto.getUnidades() < 0)
            throw new IllegalArgumentException("Las unidades son inválidas");

        if(producto.getCategoria() <= 0)
            throw new IllegalArgumentException("La categoría no coincide");
    }

    public void validarProductoNuevo(ProductoDTO producto){
        if(producto == null)
            throw new IllegalArgumentException("El producto es nulo");

        if(producto.getNombre() == null || producto.getNombre().isBlank())
            throw new IllegalArgumentException("Falta el nombre del producto");

        // 🔥 VALIDAR DUPLICADO
        if(productoRepository.existsByNombre(producto.getNombre()))
            throw new IllegalArgumentException("Ya existe un producto con ese nombre.");

        if(producto.getPrecio() == null || producto.getPrecio().compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("El precio es inválido");

        if(producto.getUnidades() < 0)
            throw new IllegalArgumentException("Las unidades no pueden ser negativas");
    }

    public void validarProductoModificado(ProductoDTO producto){
        if(producto == null)
            throw new IllegalArgumentException("El producto es nulo");

        if(producto.getNombre() == null || producto.getNombre().isBlank())
            throw new IllegalArgumentException("Falta el nombre del producto");

        if(producto.getPrecio() == null || producto.getPrecio().compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("El precio es inválido");

        if(producto.getUnidades() < 0)
            throw new IllegalArgumentException("Las unidades no pueden ser negativas");
    }

    public void validarPrecio(BigDecimal precio){
        if(precio == null)
            throw new IllegalArgumentException("El precio es inválido");
    }

    public void validarStock(int unidades){
        if(unidades < 0)
            throw new IllegalArgumentException("El stock no puede ser menor a 0");
    }
}

