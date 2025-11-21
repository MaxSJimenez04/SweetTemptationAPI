package com.sweet_temptation.api;

import com.sweet_temptation.api.dto.DetallesProductoDTO;
import com.sweet_temptation.api.dto.ProductoPedidoDTO;
import com.sweet_temptation.api.model.Pedido;
import com.sweet_temptation.api.model.Producto;
import com.sweet_temptation.api.model.ProductoPedido;
import com.sweet_temptation.api.repository.PedidoRepository;
import com.sweet_temptation.api.repository.ProductoPedidoRepository;
import com.sweet_temptation.api.repository.ProductoRepository;
import com.sweet_temptation.api.servicios.ProductoPedidoService;
import com.sweet_temptation.api.validaciones.PedidoValidator;
import com.sweet_temptation.api.validaciones.ProductoPedidoValidator;
import com.sweet_temptation.api.validaciones.ProductoValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductoPedidoServiceTest {
    @Mock
    private ProductoPedidoRepository repository;
    @Mock
    private ProductoRepository productoRepository;
    @Mock
    private PedidoRepository  pedidoRepository;
    @Mock
    private ProductoPedidoValidator validaciones = new ProductoPedidoValidator();
    @Mock
    private ProductoValidator productoValidator = new ProductoValidator();
    @Mock
    private PedidoValidator  pedidoValidator = new PedidoValidator();

    private static final Producto producto1 = new Producto(1, "Brownie", "Brownie de chocolate",
            BigDecimal.valueOf(25.00), true, 52, LocalDateTime.now(), null, 6);
    private static final Producto producto2 = new Producto(1, "Pastel de fresa", "Pastel de bizcocho de fresa con crema batida",
            BigDecimal.valueOf(25.00), true, 52, LocalDateTime.now(), null, 1);
    private static  final Producto producto3  = new Producto(1, "Pay de limón", "Pay de limón con ralladura de limón",
            BigDecimal.valueOf(25.00), true, 52, LocalDateTime.now(), null, 8);
    private final Pedido pedido1 = new Pedido(1, LocalDateTime.now(), true,
            BigDecimal.valueOf(500.50), 2, false, 1);
    private ProductoPedido productoPedido = new ProductoPedido();
    private ProductoPedido productoPedido2 = new ProductoPedido();
    private DetallesProductoDTO detalles1;
    private DetallesProductoDTO detalles2;
    List<DetallesProductoDTO> detallesList = new ArrayList<>();

    @BeforeEach
        void setUp() {
        productoPedido.setId(1);
        productoPedido.setSubtotal(BigDecimal.valueOf(100.00));
        productoPedido.setCantidad(4);
        productoPedido.setIdProducto(1);
        productoPedido.setIdPedido(1);
        productoPedido.setPrecioVenta(BigDecimal.valueOf(25.00));

        productoPedido2.setId(2);
        productoPedido2.setPrecioVenta(BigDecimal.valueOf(25.00));
        productoPedido2.setCantidad(10);
        productoPedido2.setIdProducto(3);
        productoPedido2.setIdPedido(1);
        productoPedido2.setSubtotal(BigDecimal.valueOf(250.00));

        detalles1 = new DetallesProductoDTO(
                productoPedido.getId(),
                productoPedido.getCantidad(),
                producto1.getNombre(),
                producto1.getPrecio(),
                productoPedido.getSubtotal(),
                productoPedido.getIdProducto()
        );

        detalles2 = new DetallesProductoDTO(
                productoPedido2.getId(),
                productoPedido2.getCantidad(),
                producto3.getNombre(),
                producto3.getPrecio(),
                productoPedido2.getSubtotal(),
                productoPedido2.getIdProducto()
        );

        detallesList.add(detalles1);
        detallesList.add(detalles2);
    }

    @InjectMocks
    ProductoPedidoService servicio;

    @Test
    void agregarProductoPedido_Exito(){
        //Arrange
        ProductoPedido nuevoProducto = new ProductoPedido();
        nuevoProducto.setIdPedido(pedido1.getId());
        nuevoProducto.setIdProducto(producto2.getId());
        nuevoProducto.setSubtotal(servicio.calcularSubtotal(producto2.getPrecio(), 3));
        nuevoProducto.setCantidad(3);

        when(pedidoRepository.getReferenceById(pedido1.getId())).thenReturn(pedido1);
        when(productoRepository.getReferenceById(producto2.getId())).thenReturn(producto2);
        when(repository.save(any(ProductoPedido.class))).thenReturn(nuevoProducto);

        //Act
        ProductoPedidoDTO respuesta = servicio.agregarProducto(producto2.getId(), pedido1.getId(), 3);

        //Assert
        assertEquals(3, respuesta.getCantidad());
        assertEquals(pedido1.getId(), respuesta.getIdPedido());
        assertEquals(producto2.getId(), respuesta.getIdProducto());
        assertEquals(BigDecimal.valueOf(75.00), respuesta.getSubtotal());
        verify(productoValidator, times(1)).validarIDProducto(producto2.getId());
        verify(pedidoValidator, times(1)).validarIDPedido(pedido1.getId());
        verify(validaciones, times(1)).validarCantidadPedido(3);
        verify(productoRepository, times(1)).getReferenceById(pedido1.getId());
        verify(pedidoRepository, times(1)).getReferenceById(pedido1.getId());
        verify(repository, times(1)).save(any(ProductoPedido.class));
        verify(validaciones, times(1)).validarProductoPedido(nuevoProducto);
    }

    @Test
    void agregarProductoPedido_IDPedidoInvalido(){
        //Arrange
        int idPedido = 0;
        ProductoPedido nuevoProducto = new ProductoPedido();
        nuevoProducto.setIdPedido(idPedido);
        nuevoProducto.setIdProducto(producto2.getId());
        nuevoProducto.setCantidad(3);

        //Act
        doThrow(IllegalArgumentException.class).when(pedidoValidator).validarIDPedido(nuevoProducto.getIdPedido());
        //Assert
        assertThrows(IllegalArgumentException.class, () -> servicio.agregarProducto(nuevoProducto.getIdProducto(),
                nuevoProducto.getIdPedido(), nuevoProducto.getCantidad()));
        verify(pedidoValidator, times(1)).validarIDPedido(idPedido);
    }

    @Test
    void agregarProductoPedido_IDProductoValido(){
        //Arrange
        int idProducto = 0;
        ProductoPedido nuevoProducto = new ProductoPedido();
        nuevoProducto.setIdProducto(idProducto);
        nuevoProducto.setIdPedido(pedido1.getId());
        nuevoProducto.setCantidad(3);

        //Act
        doThrow(IllegalArgumentException.class).when(productoValidator).validarIDProducto(nuevoProducto.getIdProducto());

        //Assert
        assertThrows(IllegalArgumentException.class, () -> servicio.agregarProducto(nuevoProducto.getIdProducto(),
                nuevoProducto.getIdPedido(), nuevoProducto.getCantidad()));
        verify(productoValidator, times(1)).validarIDProducto(nuevoProducto.getIdProducto());
    }

    @Test
    void agregarProductoPedido_PedidoNoExiste(){
        //Arrange
        ProductoPedido nuevoProducto = new ProductoPedido();
        nuevoProducto.setIdProducto(producto2.getId());
        nuevoProducto.setIdPedido(2);
        nuevoProducto.setCantidad(3);

        //Act
        doThrow(NoSuchElementException.class).when(pedidoValidator).validarPedido(null);

        //Arrange
        assertThrows(NoSuchElementException.class, () -> servicio.agregarProducto(nuevoProducto.getIdProducto(),
                nuevoProducto.getIdPedido(), nuevoProducto.getCantidad()));
        verify(pedidoValidator, times(1)).validarIDPedido(nuevoProducto.getIdPedido());
        verify(productoValidator, times(1)).validarIDProducto(nuevoProducto.getIdProducto());
        verify(pedidoValidator, times(1)).validarPedido(null);
    }

    @Test
    void agregarProductoPedido_ProductoNoExiste(){
        //Arrange
        ProductoPedido nuevoProducto = new ProductoPedido();
        nuevoProducto.setIdProducto(5);
        nuevoProducto.setIdPedido(pedido1.getId());
        nuevoProducto.setCantidad(3);

        //Act
        doThrow(NoSuchElementException.class).when(productoValidator).validarProducto(null);

        //Assert
        assertThrows(NoSuchElementException.class, () -> servicio.agregarProducto(nuevoProducto.getIdProducto(),
                nuevoProducto.getIdPedido(), nuevoProducto.getCantidad()));
        verify(pedidoValidator, times(1)).validarIDPedido(nuevoProducto.getIdPedido());
        verify(productoValidator, times(1)).validarIDProducto(nuevoProducto.getIdProducto());
        verify(productoValidator, times(1)).validarProducto(null);
    }

    @Test
    void agregarProductoPedido_CantidadExcedente(){
        //Arrange
        ProductoPedido nuevoProducto = new ProductoPedido();
        nuevoProducto.setIdProducto(producto2.getId());
        nuevoProducto.setIdPedido(pedido1.getId());
        nuevoProducto.setCantidad(10000);

        //Act
        when(productoRepository.getReferenceById(nuevoProducto.getIdProducto())).thenReturn(producto2);
        when(pedidoRepository.getReferenceById(nuevoProducto.getIdProducto())).thenReturn(pedido1);

        //Assert
        assertThrows(IllegalArgumentException.class, () -> servicio.agregarProducto(nuevoProducto.getIdProducto(),
                nuevoProducto.getIdPedido(), nuevoProducto.getCantidad()));

        verify(pedidoValidator, times(1)).validarIDPedido(nuevoProducto.getIdPedido());
        verify(productoValidator, times(1)).validarIDProducto(nuevoProducto.getIdProducto());
        verify(productoValidator, times(1)).validarProducto(any(Producto.class));
        verify(pedidoValidator, times(1)).validarPedido(any(Pedido.class));
    }

    @Test
    void agregarProductoPedido_ErrorCrear(){
        //Arrange
        ProductoPedido nuevoProducto = new ProductoPedido();
        nuevoProducto.setIdProducto(producto2.getId());
        nuevoProducto.setIdPedido(pedido1.getId());
        nuevoProducto.setCantidad(3);

        //Act
        when(productoRepository.getReferenceById(nuevoProducto.getIdProducto())).thenReturn(producto2);
        when(pedidoRepository.getReferenceById(nuevoProducto.getIdPedido())).thenReturn(pedido1);
        when(repository.save(nuevoProducto)).thenReturn(null);
        doThrow(RuntimeException.class).when(validaciones).validarProductoPedido(null);

        //Assert
        assertThrows(RuntimeException.class, () -> servicio.agregarProducto(nuevoProducto.getIdProducto(),
                nuevoProducto.getIdPedido(), nuevoProducto.getCantidad()));
        verify(pedidoValidator, times(1)).validarIDPedido(nuevoProducto.getIdPedido());
        verify(productoValidator, times(1)).validarIDProducto(nuevoProducto.getIdProducto());
        verify(productoValidator, times(1)).validarProducto(any(Producto.class));
        verify(pedidoValidator, times(1)).validarPedido(any(Pedido.class));
        verify(repository, times(1)).save(any(ProductoPedido.class));
    }

    @Test
    void actualizarProductoPedido_Exito(){
        //Assert
        int cantidad = 7;
        when(repository.getReferenceById(productoPedido.getId())).thenReturn(productoPedido);
        when(productoRepository.getReferenceById(productoPedido.getIdProducto())).thenReturn(producto1);
        when(repository.save(any(ProductoPedido.class))).thenReturn(productoPedido);

        //Act
        ProductoPedidoDTO respuesta = servicio.actualizarProducto(productoPedido.getId(), cantidad);

        //Assert
        assertEquals(productoPedido.getId(), respuesta.getId());
        assertEquals(cantidad, respuesta.getCantidad());
        assertEquals(productoPedido.getIdProducto(), respuesta.getIdProducto());
        assertEquals(productoPedido.getIdPedido(), respuesta.getIdPedido());
        verify(validaciones, times(1)).validarIDProductoPedido(productoPedido.getIdProducto());
        verify(validaciones, times(1)).validarCantidadPedido(cantidad);
        verify(validaciones, times(2)).validarProductoPedido(productoPedido);
        verify(productoValidator, times(1)).validarProducto(any(Producto.class));
        verify(repository, times(1)).save(productoPedido);
    }

    @Test
    void actualizarProductoPedido_IDProductoInvalido(){
        //Arrange
        productoPedido.setIdProducto(0);
        int cantidad = 2;

        //Act
        doThrow(IllegalArgumentException.class).when(validaciones).validarIDProductoPedido(productoPedido.getId());

        //Assert
        assertThrows(IllegalArgumentException.class, () -> servicio.actualizarProducto(productoPedido.getId(), cantidad));
        verify(validaciones, times(1)).validarIDProductoPedido(productoPedido.getId());
    }

    @Test
    void actualizarProductoPedido_ProductoNoExiste(){
        //Arrange
        productoPedido.setIdProducto(5);
        int cantidad = 8;

        //Act
        when(repository.getReferenceById(productoPedido.getId())).thenReturn(productoPedido);
        when(productoRepository.getReferenceById(productoPedido.getIdProducto())).thenReturn(null);
        doThrow(NoSuchElementException.class).when(productoValidator).validarProducto(null);

        //Assert
        assertThrows(NoSuchElementException.class, () -> servicio.actualizarProducto(productoPedido.getId(), cantidad));
        verify(validaciones, times(1)).validarIDProductoPedido(productoPedido.getId());
        verify(validaciones, times(1)).validarCantidadPedido(cantidad);
        verify(validaciones, times(1)).validarProductoPedido(productoPedido);
        verify(productoValidator, times(1)).validarProducto(null);
    }

    @Test
    void actualizarProductoPedido_CantidadExcedente(){
        //Arrange
        int cantidad = 777;

        //Act
        when(repository.getReferenceById(productoPedido.getId())).thenReturn(productoPedido);
        when(productoRepository.getReferenceById(productoPedido.getIdProducto())).thenReturn(producto1);

        //Assert
        assertThrows(IllegalArgumentException.class, ()-> servicio.actualizarProducto(productoPedido.getId(), cantidad));
        verify(validaciones, times(1)).validarIDProductoPedido(productoPedido.getId());
        verify(validaciones, times(1)).validarCantidadPedido(cantidad);
    }

    @Test
    void eliminarProducto_Exito(){
        //Arrange
        when(repository.getReferenceById(productoPedido.getId())).thenReturn(productoPedido);

        //Act
        servicio.eliminarProducto(productoPedido.getId());

        //Assert
        verify(validaciones, times(1)).validarIDProductoPedido(productoPedido.getId());
        verify(validaciones, times(1)).validarProductoPedido(productoPedido);
        verify(repository, times(1)).delete(productoPedido);
    }

    @Test
    void eliminarProducto_IDInvalido(){
        //Arrange
        productoPedido.setId(0);

        //Act
        doThrow(IllegalArgumentException.class).when(validaciones).validarIDProductoPedido(productoPedido.getId());

        //Assert
        assertThrows(IllegalArgumentException.class, () -> servicio.eliminarProducto(productoPedido.getId()));
    }

    @Test
    void eliminarProducto_ProductoNoExiste(){
        //Arrange
        productoPedido.setIdProducto(5);

        //Act
        when(repository.getReferenceById(productoPedido.getId())).thenReturn(null);
        doThrow(NoSuchElementException.class).when(validaciones).validarProductoPedido(null);

        //Assert
        assertThrows(NoSuchElementException.class, () -> servicio.eliminarProducto(productoPedido.getId()));
        verify(validaciones, times(1)).validarIDProductoPedido(productoPedido.getId());
        verify(validaciones, times(1)).validarProductoPedido(null);
    }

    @Test
    void obtenerListaProductos_Exito(){
        //Arrange
        when(productoRepository.obtenerListaProductos(pedido1.getId())).thenReturn(detallesList);

        //Act
        List<DetallesProductoDTO> respuesta = servicio.obtenerListaProductos(pedido1.getId());

        //Assert
        assertEquals(respuesta.size(), detallesList.size());
        verify(pedidoValidator, times(1)).validarIDPedido(pedido1.getId());
        verify(productoRepository, times(1)).obtenerListaProductos(pedido1.getId());
        verify(validaciones, times(1)).validarDetallesProductos(detallesList);
    }

    @Test
    void obtenerListaProductos_IDInvalido(){
        //Arrange
        int idPedido = 0;

        //Act
        doThrow(IllegalArgumentException.class).when(pedidoValidator).validarIDPedido(idPedido);

        //Assert
        assertThrows(IllegalArgumentException.class, () -> servicio.obtenerListaProductos(idPedido));
        verify(pedidoValidator, times(1)).validarIDPedido(idPedido);

    }

    @Test
    void obtenerListaProductos_SinProductos(){
        //Arrange
        detallesList.clear();
        when(productoRepository.obtenerListaProductos(pedido1.getId())).thenReturn(detallesList);
        //Act
        doThrow(IllegalArgumentException.class).when(validaciones).validarDetallesProductos(detallesList);

        //Assert
        assertThrows(IllegalArgumentException.class, () -> servicio.obtenerListaProductos(pedido1.getId()));
        verify(pedidoValidator, times(1)).validarIDPedido(pedido1.getId());
        verify(productoRepository, times(1)).obtenerListaProductos(pedido1.getId());

    }

    @Test
    void comprarProductos_Exito(){
        //Arrange
        when(repository.getReferenceById(productoPedido.getIdProducto())).thenReturn(productoPedido);
        when(repository.getReferenceById(productoPedido2.getIdProducto())).thenReturn(productoPedido2);

        //Act
        servicio.comprarProductos(detallesList);

        //Assert
        verify(validaciones, times(1)).validarDetallesProductos(detallesList);
        verify(validaciones, times(2)).validarDetalleProducto(any(DetallesProductoDTO.class));
        verify(validaciones, times(2)).validarProductoPedido(any(ProductoPedido.class));
    }

    @Test
    void comprarProductos_ListaVacia(){
        //Arrange
        detallesList.clear();

        //Act
        doThrow(IllegalArgumentException.class).when(validaciones).validarDetallesProductos(detallesList);

        //Assert
        assertThrows(IllegalArgumentException.class, () -> servicio.comprarProductos(detallesList));
    }

}
