package com.sweet_temptation.api.validaciones;

import com.sweet_temptation.api.dto.DetallesProductoDTO;
import com.sweet_temptation.api.model.Producto;
import com.sweet_temptation.api.model.ProductoPedido;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;

@Component
public class ProductoPedidoValidator {
    public void validarIDProductoPedido(int id){
        if (id <= 0){
            throw new IllegalArgumentException("ID de producto inválido");
        }
    }

    public void validarProductoPedido(ProductoPedido producto){
        if(producto == null){
            throw new RuntimeException("El producto no ha sido agregado al carrito");
        }
    }
    public void validarCantidadPedido(int cantidad){
        if (cantidad < 0){
            throw new ArrayIndexOutOfBoundsException("La cantidad de productos no puede ser menor que 0");
        }
    }

    public void validarDetallesProductos(List<DetallesProductoDTO> productosCompra){
        if(productosCompra.isEmpty()){
            throw new IllegalArgumentException("No hay productos comprados");
        }
    }

    public void validarDetalleProducto(DetallesProductoDTO dto){
        if(dto == null || dto.getCantidad() <= 0 || dto.getId() <= 0 || dto.getIdProducto() <= 0){
            throw new IllegalArgumentException("El producto tiene campos inválidos o está vacio");
        }
    }
}
