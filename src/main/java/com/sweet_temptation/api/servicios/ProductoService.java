package com.sweet_temptation.api.servicios;

import com.sweet_temptation.api.dto.ProductoDTO;
import com.sweet_temptation.api.model.Producto;
import com.sweet_temptation.api.repository.ProductoRepository;
import com.sweet_temptation.api.validaciones.ProductoValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        // validaciones.validarIDProducto(idProducto);
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

    public ProductoDTO crearProducto(ProductoDTO producto){
        validaciones.validarProductoNuevo(producto);

        Producto nuevo = new Producto();
        nuevo.setNombre(producto.getNombre());
        nuevo.setDescripcion(producto.getDescripcion());
        nuevo.setPrecio(producto.getPrecio());
        //nuevo.setDisponible(producto.getDisponible());
        nuevo.setUnidades(producto.getUnidades());
        nuevo.setIdCategoria(producto.getCategoria());
        nuevo.setFechaRegistro(LocalDateTime.now());
        nuevo.setFechaModificacion(LocalDateTime.now());

        Producto guardado = productoRepository.save(nuevo);
        validaciones.validarProducto(guardado);
        return  toDTO(guardado);
    }

    public ProductoDTO actualizarProducto(int idProducto, ProductoDTO producto){
        // TODO - Agregar validacion para el id del producto que se quiere modificar
        Producto actual = productoRepository.findByID(idProducto).orElseThrow(() -> new NoSuchElementException("El producto no fue encontrado"));

        // TODO - Agregar otra validacion para el producto actualizado

        actual.setNombre(producto.getNombre());
        actual.setDescripcion(producto.getDescripcion());
        actual.setPrecio(producto.getPrecio());
        //actual.setDisponible(producto.getDisponible());
        actual.setUnidades(producto.getUnidades());
        actual.setIdCategoria(producto.getCategoria());
        actual.setFechaModificacion(LocalDateTime.now());

        Producto actualizado= productoRepository.save(actual);
        return  toDTO(actualizado);
    }

    public void eliminarProducto(int idProductoEliminar) {
        // TODO - Agregar validacion para el id del producto
        Producto p = productoRepository.getReferenceById(idProductoEliminar);
        validaciones.validarProducto(p);
        productoRepository.delete(p);
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
                producto.getFechaModificacion()
                // TODO - agregar la categoria
        );
    }
}
