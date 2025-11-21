package com.sweet_temptation.api.servicios;

import com.sweet_temptation.api.dto.ProductoDTO;
import com.sweet_temptation.api.model.Producto;
import com.sweet_temptation.api.repository.ProductoRepository;
import com.sweet_temptation.api.validaciones.ProductoValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProductoService {
    private final ProductoRepository productoRepository;
    private final ProductoValidator validaciones;

    public ProductoService(ProductoRepository productoRepository, ProductoValidator validaciones) {
        this.productoRepository = productoRepository;
        this.validaciones = validaciones;
    }

    @Transactional(readOnly = true)
    public ProductoDTO consultarProducto(int idProducto){
        validaciones.validarIDProducto(idProducto);
        Producto pro = productoRepository.findById(idProducto)
                .orElseThrow(() -> new NoSuchElementException("Producto no encontrado: " + idProducto));
        validaciones.validarProducto(pro);
        return toDTO(pro);
    }

    @Transactional(readOnly = true)
    public List<ProductoDTO> consultarProductos(){
        return productoRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<ProductoDTO> consultarPorCategoria(int idCategoria){
        // TODO - Hacer una validacion para la categoria
        List<Producto> lista = productoRepository.findByCategoria(idCategoria);
        if(lista == null || lista.isEmpty()){
            throw new NoSuchElementException("No se encontraron productos para la categoria");
        }
        return  lista.stream().map(this::toDTO).toList();
    }

    public int crearProducto(ProductoDTO producto){
        validaciones.validarProductoNuevo(producto);

        Producto nuevo = new Producto();
        nuevo.setNombre(producto.getNombre());
        nuevo.setDescripcion(producto.getDescripcion());
        nuevo.setPrecio(producto.getPrecio());
        nuevo.setDisponible(producto.getDisponible());
        nuevo.setUnidades(producto.getUnidades());
        nuevo.setIdCategoria(producto.getCategoria());
        nuevo.setFechaRegistro(LocalDateTime.now());
        nuevo.setFechaModificacion(LocalDateTime.now());

        Producto guardado = productoRepository.save(nuevo);
        validaciones.validarProducto(guardado);
        //return  toDTO(guardado);

        return guardado.getId();
    }

    public ProductoDTO actualizarProducto(int idProducto, ProductoDTO producto){
        validaciones.validarIDProducto(idProducto);
        Producto actual = productoRepository.findById(idProducto)
                .orElseThrow(() -> new NoSuchElementException("Producto no encontrado: " + idProducto));

        actual.setNombre(producto.getNombre());
        actual.setDescripcion(producto.getDescripcion());
        actual.setPrecio(producto.getPrecio());
        actual.setDisponible(producto.getDisponible());
        actual.setUnidades(producto.getUnidades());
        actual.setIdCategoria(producto.getCategoria());
        actual.setFechaModificacion(LocalDateTime.now());

        Producto actualizado= productoRepository.save(actual);
        return  toDTO(actualizado);
    }

    public ProductoDTO actualizarPrecio(int idProducto, BigDecimal precioNuevo){
        validaciones.validarIDProducto(idProducto);
        validaciones.validarPrecio(precioNuevo);

        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new NoSuchElementException("Producto no encontrado: " + idProducto));

        producto.setPrecio(precioNuevo);
        producto.setFechaModificacion(LocalDateTime.now());

        return toDTO(productoRepository.save(producto));
    }

    public ProductoDTO modificarInventario(int idProducto, int unidadesProducto){
        validaciones.validarIDProducto(idProducto);
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new NoSuchElementException("Producto no encontrado: " + idProducto));

        int nuevoStock = producto.getUnidades() + unidadesProducto;
        validaciones.validarStock(nuevoStock);

        producto.setUnidades(nuevoStock);
        producto.setFechaModificacion(LocalDateTime.now());

        return toDTO(productoRepository.save(producto));
    }

    public ProductoDTO cambiarDisponibilidad(int idProducto, boolean disponible){
        validaciones.validarIDProducto(idProducto);
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new NoSuchElementException("Producto no encontrado: " + idProducto));

        producto.setDisponible(disponible);
        producto.setFechaModificacion(LocalDateTime.now());

        return toDTO(productoRepository.save(producto));
    }

    public void eliminarProducto(int idProductoEliminar) {
        validaciones.validarIDProducto(idProductoEliminar);
        //productoRepository.existsById(idProductoEliminar);
        if (!productoRepository.existsById(idProductoEliminar)) {
            throw new NoSuchElementException("Producto no encontrado: " + idProductoEliminar);
        }
        productoRepository.deleteById(idProductoEliminar);
    }

    private ProductoDTO toDTO(Producto producto){
        return new ProductoDTO(
                producto.getId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getDisponible(),
                producto.getUnidades(),
                producto.getFechaRegistro(),
                producto.getFechaModificacion(),
                producto.getIdCategoria()
        );
    }
}
