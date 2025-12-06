package com.sweet_temptation.api.servicios;

import com.sweet_temptation.api.dto.ProductoDTO;
import com.sweet_temptation.api.model.Producto;
import com.sweet_temptation.api.repository.ProductoRepository;
import com.sweet_temptation.api.repository.ProductoPedidoRepository;
import com.sweet_temptation.api.validaciones.ProductoValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final ProductoValidator validaciones;
    private final ArchivoService archivoService;
    private final ProductoPedidoRepository productoPedidoRepository;

    @PersistenceContext
    private EntityManager entityManager;


    public ProductoService(ProductoRepository productoRepository,
                           ProductoValidator validaciones,
                           ArchivoService archivoService,
                           ProductoPedidoRepository productoPedidoRepository) {
        this.productoRepository = productoRepository;
        this.validaciones = validaciones;
        this.archivoService = archivoService;
        this.productoPedidoRepository = productoPedidoRepository;
    }

    @Transactional(readOnly = true)
    public ProductoDTO consultarProducto(int idProducto) {
        validaciones.validarIDProducto(idProducto);

        Producto pro = productoRepository.findById(idProducto)
                .orElseThrow(() -> new NoSuchElementException("Producto no encontrado: " + idProducto));

        validaciones.validarProducto(pro);

        return toDTO(pro);
    }

    @Transactional(readOnly = true)
    public List<ProductoDTO> consultarProductos() {
        return productoRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProductoDTO> consultarPorCategoria(int idCategoria) {

        List<Producto> lista = productoRepository.findByCategoria(idCategoria);

        if (lista == null || lista.isEmpty()) {
            throw new NoSuchElementException("No se encontraron productos para la categoría indicada.");
        }

        return lista.stream().map(this::toDTO).toList();
    }

    public int crearProducto(ProductoDTO producto) {

        validaciones.validarProductoNuevo(producto);

        Producto nuevo = new Producto();
        nuevo.setNombre(producto.getNombre());
        nuevo.setDescripcion(producto.getDescripcion());
        nuevo.setPrecio(producto.getPrecio());
        nuevo.setDisponible(producto.getDisponible());
        nuevo.setUnidades(producto.getUnidades());
        nuevo.setCategoria(producto.getCategoria());
        nuevo.setFechaRegistro(LocalDateTime.now());
        nuevo.setFechaModificacion(LocalDateTime.now());

        Producto guardado = productoRepository.save(nuevo);

        validaciones.validarProducto(guardado);

        return guardado.getId();
    }

    public ProductoDTO actualizarProducto(int idProducto, ProductoDTO producto) {

        validaciones.validarIDProducto(idProducto);
        validaciones.validarProductoModificado(producto);

        Producto actual = productoRepository.findById(idProducto)
                .orElseThrow(() -> new NoSuchElementException("Producto no encontrado: " + idProducto));

        actual.setNombre(producto.getNombre());
        actual.setDescripcion(producto.getDescripcion());
        actual.setPrecio(producto.getPrecio());
        actual.setDisponible(producto.getDisponible());
        actual.setUnidades(producto.getUnidades());
        actual.setCategoria(producto.getCategoria());
        actual.setFechaModificacion(LocalDateTime.now());

        Producto actualizado = productoRepository.save(actual);

        return toDTO(actualizado);
    }

    public ProductoDTO actualizarPrecio(int idProducto, BigDecimal precioNuevo) {

        validaciones.validarIDProducto(idProducto);
        validaciones.validarPrecio(precioNuevo);

        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new NoSuchElementException("Producto no encontrado"));

        producto.setPrecio(precioNuevo);
        producto.setFechaModificacion(LocalDateTime.now());

        return toDTO(productoRepository.save(producto));
    }

    public ProductoDTO modificarInventario(int idProducto, int unidadesProducto) {

        validaciones.validarIDProducto(idProducto);

        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new NoSuchElementException("Producto no encontrado: " + idProducto));

        int nuevoStock = producto.getUnidades() + unidadesProducto;

        validaciones.validarStock(nuevoStock);

        producto.setUnidades(nuevoStock);
        producto.setFechaModificacion(LocalDateTime.now());

        return toDTO(productoRepository.save(producto));
    }

    public ProductoDTO cambiarDisponibilidad(int idProducto, boolean disponible) {

        validaciones.validarIDProducto(idProducto);

        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new NoSuchElementException("Producto no encontrado"));

        producto.setDisponible(disponible);
        producto.setFechaModificacion(LocalDateTime.now());

        return toDTO(productoRepository.save(producto));
    }

    @Transactional
    public void limpiarPedidosRelacionados(int idProducto) {
        // Usamos SQL Nativo para eliminar referencias en ProductoPedido (tabla muchos-a-muchos)
        // Esto es la solución más robusta contra errores de mapeo JPA/Hibernate al inicio.
        entityManager.createNativeQuery(
                        "DELETE FROM ProductoPedido WHERE idProducto = :idProducto")
                .setParameter("idProducto", idProducto)
                .executeUpdate();
    }

    @Transactional
    public void eliminarProducto(int idProductoEliminar) {

        validaciones.validarIDProducto(idProductoEliminar);

        if (!productoRepository.existsById(idProductoEliminar)) {
            throw new NoSuchElementException("Producto no encontrado");
        }

        archivoService.eliminarAsociacionYArchivoPorProducto(idProductoEliminar);

        limpiarPedidosRelacionados(idProductoEliminar);

        productoRepository.deleteById(idProductoEliminar);
    }

    @Transactional(readOnly = true)
    public ProductoDTO consultarProductoPorNombre(String nombre) {

        List<Producto> productos = productoRepository.findByNombre(nombre);

        if (productos.isEmpty()) {
            throw new NoSuchElementException("Producto no encontrado con nombre: " + nombre);
        }

        return toDTO(productos.get(0));
    }

    private ProductoDTO toDTO(Producto producto) {
        return new ProductoDTO(
                producto.getId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getDisponible(),
                producto.getUnidades(),
                producto.getFechaRegistro(),
                producto.getFechaModificacion(),
                producto.getCategoria()
        );
    }
}