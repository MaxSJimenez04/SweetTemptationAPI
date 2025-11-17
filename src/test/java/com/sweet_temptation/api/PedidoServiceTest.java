package com.sweet_temptation.api;


import com.sweet_temptation.api.dto.PedidoDTO;
import com.sweet_temptation.api.model.Pedido;
import com.sweet_temptation.api.model.Usuario;
import com.sweet_temptation.api.repository.PedidoRepository;
import com.sweet_temptation.api.servicios.PedidoService;
import com.sweet_temptation.api.validaciones.PedidoValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@ExtendWith(MockitoExtension.class)
public class PedidoServiceTest {
    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private PedidoValidator validaciones;

    @InjectMocks
    private PedidoService pedidoService;

    private Pedido pedidoActual;
    private Pedido pedido1;
    private Pedido pedido2;
    private Pedido pedido3;
    private Usuario empleado;
    private Usuario cliente;
    private List<Pedido> pedidos = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        cliente = new Usuario();
        cliente.setId(1);
        cliente.setIdRol(3);
        empleado = new Usuario();
        empleado.setId(2);
        empleado.setIdRol(2);
        pedidoActual = new Pedido(1, LocalDateTime.now(), true, BigDecimal.valueOf(100), 2,
                false, cliente.getId());
        pedido1 = new Pedido(1, LocalDateTime.now(), true, BigDecimal.valueOf(100), 2,
                false, empleado.getId());
        pedido2 = new Pedido(2, LocalDateTime.now(), true, BigDecimal.valueOf(200), 2,
                false, empleado.getId());
        pedido3 = new Pedido(3, LocalDateTime.now(), true, BigDecimal.valueOf(300), 2,
                false, empleado.getId());
        pedidos.add(pedido1);
        pedidos.add(pedido2);
        pedidos.add(pedido3);
    }


    @Test
    void consultarPedidoActual_Exito(){
        //Arrange
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setIdRol(3);

        when(pedidoRepository.findByActualTrueAndIdCliente(cliente.getId())).thenReturn(pedidoActual);

       //Act
       PedidoDTO respuesta = pedidoService.consultarPedidoActual(cliente.getId());

       //Assert
        assertEquals(pedidoActual.getId(), respuesta.getId());
        assertEquals(pedidoActual.getActual(), respuesta.getActual());
        assertEquals(pedidoActual.getTotal(), respuesta.getTotal());
        assertEquals(pedidoActual.getEstado(), respuesta.getEstado());
        assertEquals(pedidoActual.getFechaCompra(), respuesta.getFechaCompra());
        assertEquals(pedidoActual.getPersonalizado(), respuesta.getPersonalizado());
        verify(pedidoRepository, times(1)).findByActualTrueAndIdCliente(1);
        verify(validaciones, times(1)).validarIDCliente(1);
        verify(validaciones, times(1)).validarPedido(pedidoActual);
    }

    @Test
    void consultarPedidoActual_IDInvalido(){
        //Arrange
        cliente.setId(0);

        //Act
        doThrow(IllegalArgumentException.class).when(validaciones).validarIDCliente(cliente.getId());

        //Assert
        assertThrows(IllegalArgumentException.class, () -> pedidoService.consultarPedidoActual(cliente.getId()));
        verify(validaciones, times(1)).validarIDCliente(cliente.getId());
    }

    @Test
    void consultarPedidoActual_PedidoNoExiste(){
        //Arrange
        pedidoActual.setActual(false);

        //Act
        when(pedidoRepository.findByActualTrueAndIdCliente(cliente.getId())).thenReturn(null);
        doThrow(NoSuchElementException.class).when(validaciones).validarPedido(null);

        //Assert
        assertThrows(NoSuchElementException.class, () -> pedidoService.consultarPedidoActual(cliente.getId()));
        verify(validaciones, times(1)).validarIDCliente(cliente.getId());
        verify(pedidoRepository, times(1)).findByActualTrueAndIdCliente(cliente.getId());
        verify(validaciones, times(1)).validarPedido(null);
    }

    @Test
    void consultarPedidosActuales_Exito(){
        //Arrange
        when(pedidoRepository.findByIdClienteAndEstado(empleado.getId(),2)).thenReturn(pedidos);

        //Act
        List<PedidoDTO> respuesta = pedidoService.consultarPedidosActuales(empleado.getId());

        //Assert
        assertEquals(pedidos.size(), respuesta.size());
        verify(validaciones, times(1)).validarIDCliente(empleado.getId());
        verify(pedidoRepository, times(1)).findByIdClienteAndEstado(empleado.getId(),2);
    }

    @Test
    void consultarPedidosActuales_IDInvalido(){
        //Arrange
        empleado.setId(0);

        //Act
        doThrow(IllegalArgumentException.class).when(validaciones).validarIDCliente(empleado.getId());

        //Assert
        assertThrows(IllegalArgumentException.class, () -> pedidoService.consultarPedidosActuales(empleado.getId()));
        verify(validaciones, times(1)).validarIDCliente(empleado.getId());
    }

    @Test
    void consultarPedidosActuales_NoHayPedidos(){
        //Arrange
        pedidos.clear();
        //Act
        when(pedidoRepository.findByIdClienteAndEstado(empleado.getId(),2)).thenReturn(null);

        //Assert
        assertThrows(NoSuchElementException.class, () -> pedidoService.consultarPedidosActuales(empleado.getId()));
        verify(validaciones, times(1)).validarIDCliente(empleado.getId());
        verify(pedidoRepository, times(1)).findByIdClienteAndEstado(empleado.getId(),2);
    }

    @Test
    void consularPedido_Exito(){
        //Arrange
        int id = 1;
        when(pedidoRepository.getReferenceById(id)).thenReturn(pedido1);

        //Act
        PedidoDTO respuesta = pedidoService.consultarPedido(id);

        //Assert
        assertEquals(pedido1.getId(), respuesta.getId());
        assertEquals(pedido1.getFechaCompra(), respuesta.getFechaCompra());
        assertEquals(pedido1.getActual(), respuesta.getActual());
        assertEquals(pedido1.getTotal(), respuesta.getTotal());
        assertEquals(pedido1.getEstado(), respuesta.getEstado());
        assertEquals(pedido1.getPersonalizado(), respuesta.getPersonalizado());
        verify(pedidoRepository, times(1)).getReferenceById(id);
        verify(validaciones, times(1)).validarIDPedido(id);
        verify(validaciones, times(1)).validarPedido(any(Pedido.class));

    }

    @Test
    void consularPedido_IDInvalido(){
        //Arrange
        int id = 0;

        //Act
        doThrow(IllegalArgumentException.class).when(validaciones).validarIDPedido(id);

        //Assert
        assertThrows(IllegalArgumentException.class, () -> pedidoService.consultarPedido(id));
        verify(validaciones, times(1)).validarIDPedido(id);
    }

    @Test
    void consularPedido_PedidoNoExiste(){
        //Arrange
        int id = 5;
        when(pedidoRepository.getReferenceById(id)).thenReturn(null);

        //Act
        doThrow(NoSuchElementException.class).when(validaciones).validarPedido(null);

        //Assert
        assertThrows(NoSuchElementException.class, () -> pedidoService.consultarPedido(id));
        verify(validaciones, times(1)).validarIDPedido(id);
        verify(pedidoRepository, times(1)).getReferenceById(id);
        verify(validaciones, times(1)).validarPedido(null);
    }

    @Test
    void crearPedidoCliente_Exito(){
        //Act
        pedidoService.crearPedidoCliente(cliente.getId());

        //Assert
        verify(validaciones, times(1)).validarIDCliente(cliente.getId());
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    void crearPedidoCliente_IDInvalido(){
        //Arrange
        cliente.setId(0);

        //Act
        doThrow(IllegalArgumentException.class).when(validaciones).validarIDCliente(cliente.getId());

        //Assert
        assertThrows(IllegalArgumentException.class, () -> pedidoService.crearPedidoCliente(cliente.getId()));
        verify(validaciones, times(1)).validarIDCliente(cliente.getId());
    }

    @Test
    void crearPedidoEmpleado_Exito(){
        //Act
        PedidoDTO respuesta =  pedidoService.crearPedidoEmpleado(empleado.getId());

        //Assert
        assertEquals(empleado.getId(),respuesta.getIdCliente());
        assertEquals(false, respuesta.getPersonalizado());
        assertEquals(1, respuesta.getEstado());
        assertEquals(BigDecimal.ZERO, respuesta.getTotal());
        verify(validaciones, times(1)).validarIDCliente(empleado.getId());
        verify(validaciones, times(1)).validarPedido(null);
    }

    @Test
    void crearPedidoEmpleado_IDInvalido(){
        //Arrange
        empleado.setId(0);

        //Act
        doThrow(IllegalArgumentException.class).when(validaciones).validarIDCliente(empleado.getId());

        //Assert
        assertThrows(IllegalArgumentException.class, () -> pedidoService.crearPedidoEmpleado(empleado.getId()));
        verify(validaciones, times(1)).validarIDCliente(empleado.getId());
    }

    @Test
    void cambiarTotalPedido_Exito(){
        //Arrange
        BigDecimal nuevoTotal = BigDecimal.valueOf(500);

        when(pedidoRepository.getReferenceById(pedidoActual.getId())).thenReturn(pedidoActual);
        when(pedidoRepository.save(pedidoActual)).thenReturn(pedidoActual);

        //Act
        PedidoDTO respuesta = pedidoService.cambiarTotalPedido(pedidoActual.getId(), nuevoTotal);

        //Assert
        assertEquals(nuevoTotal, respuesta.getTotal());
        verify(validaciones, times(1)).validarIDPedido(pedidoActual.getId());
        verify(validaciones, times(1)).validarPedido(any(Pedido.class));
        verify(pedidoRepository, times(1)).getReferenceById(pedidoActual.getId());
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    void cambiarTotalPedido_IDInvalido(){
        //Arrange
        pedidoActual.setId(0);
        BigDecimal nuevoTotal = BigDecimal.valueOf(500);

        //Act
        doThrow(IllegalArgumentException.class).when(validaciones).validarIDPedido(pedidoActual.getId());

        //Assert
        assertThrows(IllegalArgumentException.class, () -> pedidoService.cambiarTotalPedido(pedidoActual.getId(),  nuevoTotal));
        verify(validaciones, times(1)).validarIDPedido(pedidoActual.getId());
    }

    @Test
    void cambiarTotalPedido_PedidoNoExiste(){
        //Arrange
        int id = 1;
        BigDecimal nuevoTotal = BigDecimal.valueOf(200);

        //Act
        doThrow(NoSuchElementException.class).when(validaciones).validarPedido(null);

        //Assert
        assertThrows(NoSuchElementException.class, () -> pedidoService.cambiarTotalPedido(id,  nuevoTotal));
        verify(validaciones, times(1)).validarIDPedido(id);
        verify(pedidoRepository, times(1)).getReferenceById(id);
    }

    @Test
    void completarPedido_Exito(){
        //Arrange
        when(pedidoRepository.getReferenceById(1)).thenReturn(pedidoActual);
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedidoActual);

        //Act
         pedidoService.cancelarPedido(pedidoActual.getId());

        //Assert
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
        verify(validaciones, times(1)).validarIDPedido(pedidoActual.getId());
        verify(validaciones, times(1)).validarPedido(any(Pedido.class));
    }

    @Test
    void completarPedido_IDInvalido(){
        //Arrange
        pedidoActual.setId(0);

        //Act
        doThrow(IllegalArgumentException.class).when(validaciones).validarIDPedido(pedidoActual.getId());

        //Assert
        assertThrows(IllegalArgumentException.class, () -> pedidoService.completarPedido(pedidoActual.getId()));
        verify(validaciones, times(1)).validarIDPedido(pedidoActual.getId());
    }

    @Test
    void completarPedido_PedidoNoExiste(){
        //Arrange
        int id = 90;

        //Act
        doThrow(NoSuchElementException.class).when(validaciones).validarPedido(null);

        //Assert
        assertThrows(NoSuchElementException.class,() -> pedidoService.completarPedido(id));
        verify(validaciones, times(1)).validarIDPedido(id);
        verify(pedidoRepository, times(1)).getReferenceById(id);
    }

    @Test
    void eliminarPedido_Exito(){
        //Arrange
        when(pedidoRepository.getReferenceById(1)).thenReturn(pedidoActual);

        //Act
        pedidoService.eliminarPedido(pedidoActual.getId());

        //Assert
        verify(pedidoRepository, times(1)).delete(any(Pedido.class));
        verify(validaciones, times(1)).validarIDPedido(pedidoActual.getId());
        verify(validaciones, times(1)).validarPedido(any(Pedido.class));
    }

    @Test
    void eliminarPedido_IDInvalido(){
        //Arrange
        pedidoActual.setId(0);

        //Act
        doThrow(IllegalArgumentException.class).when(validaciones).validarIDPedido(pedidoActual.getId());

        //Assert
        assertThrows(IllegalArgumentException.class,() -> pedidoService.eliminarPedido(pedidoActual.getId()));
        verify(validaciones, times(1)).validarIDPedido(pedidoActual.getId());
    }

    @Test
    void eliminarPedido_PedidoNoExiste(){
        //Arrange
        int id = 5;

        //Act
        when(pedidoRepository.getReferenceById(id)).thenReturn(null);
        doThrow(NoSuchElementException.class).when(validaciones).validarPedido(null);

        //Assert
        assertThrows(NoSuchElementException.class, () ->  pedidoService.eliminarPedido(id));
    }

}
