package com.sweet_temptation.api.servicios;

import com.sweet_temptation.api.dto.DetallesProductoDTO;
import com.sweet_temptation.api.dto.ProductoPedidoDTO;
import com.sweet_temptation.api.model.Pedido;
import com.sweet_temptation.api.model.Producto;
import com.sweet_temptation.api.model.ProductoPedido;
import com.sweet_temptation.api.repository.PedidoRepository;
import com.sweet_temptation.api.repository.ProductoPedidoRepository;
import com.sweet_temptation.api.repository.ProductoRepository;
import com.sweet_temptation.api.validaciones.PedidoValidator;
import com.sweet_temptation.api.validaciones.ProductoPedidoValidator;
import com.sweet_temptation.api.validaciones.ProductoValidator;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductoPedidoService {
    private final ProductoPedidoRepository repository;
    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;
    private final ProductoPedidoValidator validaciones;
    private final ProductoValidator productoValidator;
    private final PedidoValidator pedidoValidator;


    public ProductoPedidoService(PedidoRepository pedidoRepository,  ProductoPedidoRepository repository,
                                 ProductoRepository productoRepository, ProductoPedidoValidator validaciones,
                                 ProductoValidator productoValidator, PedidoValidator pedidoValidator) {
        this.repository = repository;
        this.pedidoRepository = pedidoRepository;
        this.productoRepository = productoRepository;
        this.validaciones = validaciones;
        this.productoValidator = productoValidator;
        this.pedidoValidator = pedidoValidator;
    }

    public ProductoPedidoDTO agregarProducto(int idProducto, int idPedido, int cantidad) {
        productoValidator.validarIDProducto(idProducto);
        pedidoValidator.validarIDPedido(idPedido);
        validaciones.validarCantidadPedido(cantidad);
        Producto productoBD = productoRepository.getReferenceById(idProducto);
        Pedido pedidoBD = pedidoRepository.getReferenceById(idPedido);
        productoValidator.validarProducto(productoBD);
        pedidoValidator.validarPedido(pedidoBD);
        ProductoPedido nuevoProducto = new ProductoPedido();

        nuevoProducto.setIdProducto(idProducto);
        nuevoProducto.setCantidad(cantidad);
        nuevoProducto.setSubtotal(calcularSubtotal(productoBD.getPrecio(), cantidad));
        nuevoProducto.setIdPedido(idPedido);

        ProductoPedido respuesta = repository.save(nuevoProducto);
        validaciones.validarProductoPedido(respuesta);
        return new ProductoPedidoDTO(respuesta.getId(), respuesta.getSubtotal(), respuesta.getCantidad(),
                respuesta.getPrecioVenta(), respuesta.getIdPedido(), respuesta.getIdProducto());
    }

    public ProductoPedidoDTO actualizarProducto(int idProducto, int cantidad) {
        validaciones.validarIDProductoPedido(idProducto);
        validaciones.validarCantidadPedido(cantidad);
        ProductoPedido productoBD = repository.getReferenceById(idProducto);
        validaciones.validarProductoPedido(productoBD);
        Producto productoOriginal = productoRepository.getReferenceById(productoBD.getIdProducto());
        productoValidator.validarProducto(productoOriginal);
        if(productoOriginal.getUnidades() <= cantidad) {
            throw new IllegalArgumentException("Cantidad mayor a la disponible");
        }
        productoBD.setCantidad(cantidad);
        productoBD.setSubtotal(calcularSubtotal(productoBD.getPrecioVenta(), cantidad));
        ProductoPedido respuesta = repository.save(productoBD);
        validaciones.validarProductoPedido(respuesta);
        return new ProductoPedidoDTO(respuesta.getId(), respuesta.getSubtotal(), respuesta.getCantidad(),
                respuesta.getPrecioVenta(), respuesta.getIdPedido(), respuesta.getIdProducto());
    }

    public void eliminarProducto(int idProducto) {
        validaciones.validarIDProductoPedido(idProducto);
        ProductoPedido pedidoBD = repository.getReferenceById(idProducto);
        validaciones.validarProductoPedido(pedidoBD);
        repository.delete(pedidoBD);
    }

    public List<DetallesProductoDTO> obtenerListaProductos(int idPedido){
        pedidoValidator.validarIDPedido(idPedido);
        List<DetallesProductoDTO> productosBD = productoRepository.obtenerListaProductos(idPedido);
        validaciones.validarDetallesProductos(productosBD);
        return productosBD;
    }

    public void comprarProductos(List<DetallesProductoDTO> productosCompra){
        validaciones.validarDetallesProductos(productosCompra);
        for(DetallesProductoDTO productoCompra : productosCompra){
            validaciones.validarDetalleProducto(productoCompra);
            ProductoPedido pedidoBD = repository.getReferenceById(productoCompra.getIdProducto());
            validaciones.validarProductoPedido(pedidoBD);
            pedidoBD.setPrecioVenta(productoCompra.getPrecio());
            repository.save(pedidoBD);
        }
    }

    public BigDecimal calcularSubtotal(BigDecimal precio, int cantidad){
        BigDecimal parseo = BigDecimal.valueOf(cantidad);
        return precio.multiply(parseo);
    }

}
