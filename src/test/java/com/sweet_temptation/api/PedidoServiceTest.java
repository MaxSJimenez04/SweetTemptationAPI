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
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class PedidoServiceTest {
    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private PedidoValidator validaciones;

    @InjectMocks
    private PedidoService pedidoService;

    @Test
    void consultarPedidoActual_Exito(){
        //Arrange
        Pedido pedidoActual = new Pedido();
        pedidoActual.setId(1);
        pedidoActual.setActual(true);
        pedidoActual.setTotal(BigDecimal.valueOf(100));
        pedidoActual.setIdCliente(1);
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setIdRol(3);

        doNothing().when(validaciones).validarIDCliente(1);
        when(pedidoRepository.findByActualTrueAndIdCliente(1)).thenReturn(pedidoActual);
        doNothing().when(validaciones).validarPedido(pedidoActual);

       //Act
       PedidoDTO respuesta = pedidoService.consultarPedidoActual(1);

       //Assert
       assertEquals(BigDecimal.valueOf(100), respuesta.getTotal());
       //Verifica que se llamó a los métodos 1 vez
        verify(pedidoRepository, times(1)).findByActualTrueAndIdCliente(1);
        verify(validaciones, times(1)).validarIDCliente(1);
    }

    @Test
    void consultarPedidoActual_IDInvalido(){
        //Arrange
        Pedido pedidoActual = new Pedido();
        pedidoActual.setId(1);
        pedidoActual.setActual(true);
        pedidoActual.setTotal(BigDecimal.valueOf(100));
        pedidoActual.setIdCliente(1);
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setIdRol(3);

        //Act
        doThrow(IllegalArgumentException.class).when(validaciones).validarIDCliente(0);

        //Assert
        assertThrows(IllegalArgumentException.class, () -> pedidoService.consultarPedidoActual(0));
        verify(validaciones, times(1)).validarIDCliente(0);
    }

    @Test
    void consultarPedidoActual_PedidoNoExiste(){
        //Arrange
        Pedido pedidoActual = new Pedido();
        pedidoActual.setId(1);
        pedidoActual.setActual(false);
        pedidoActual.setTotal(BigDecimal.valueOf(100));
        pedidoActual.setIdCliente(1);
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setIdRol(3);

        //Act
        doNothing().when(validaciones).validarIDCliente(1);
        when(pedidoRepository.findByActualTrueAndIdCliente(1)).thenReturn(null);
        doThrow(NoSuchElementException.class).when(validaciones).validarPedido(null);

        //Assert
        assertThrows(NoSuchElementException.class, () -> pedidoService.consultarPedidoActual(1));
        verify(validaciones, times(1)).validarIDCliente(1);
        verify(pedidoRepository, times(1)).findByActualTrueAndIdCliente(1);
        verify(validaciones, times(1)).validarPedido(null);
    }

    @Test
    void consultarPedidosActuales_Exito(){
        //Arrange
        Pedido pedido1 = new Pedido();
        Pedido pedido2 = new Pedido();
        Pedido pedido3 = new Pedido();
        pedido1.setId(1);
        pedido1.setActual(true);
        pedido1.setEstado(2);
        pedido1.setTotal(BigDecimal.valueOf(100));
        pedido1.setIdCliente(1);
        pedido2.setId(2);
        pedido2.setActual(true);
        pedido2.setEstado(2);
        pedido2.setTotal(BigDecimal.valueOf(200));
        pedido2.setIdCliente(1);
        pedido3.setId(3);
        pedido3.setActual(true);
        pedido3.setEstado(2);
        pedido3.setTotal(BigDecimal.valueOf(300));
        pedido3.setIdCliente(1);
        List<Pedido> pedidosEmpleado = new ArrayList<>();
        pedidosEmpleado.add(pedido1);
        pedidosEmpleado.add(pedido2);
        pedidosEmpleado.add(pedido3);
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setIdRol(2);

        //Act
        doNothing().when(validaciones).validarIDCliente(1);
        when(pedidoRepository.findByIdClienteAndEstado(1,2)).thenReturn(pedidosEmpleado);
        List<PedidoDTO> respuesta = pedidoService.consultarPedidosActuales(1);

        //Assert
        assertEquals(3, respuesta.size());
        verify(validaciones, times(1)).validarIDCliente(1);
        verify(pedidoRepository, times(1)).findByIdClienteAndEstado(1,2);
    }

    @Test
    void consultarPedidosActuales_IDInvalido(){
        //Arrange
        Pedido pedido1 = new Pedido();
        Pedido pedido2 = new Pedido();
        Pedido pedido3 = new Pedido();
        pedido1.setId(1);
        pedido1.setActual(true);
        pedido1.setEstado(2);
        pedido1.setTotal(BigDecimal.valueOf(100));
        pedido1.setIdCliente(1);
        pedido2.setId(2);
        pedido2.setActual(true);
        pedido2.setEstado(2);
        pedido2.setTotal(BigDecimal.valueOf(200));
        pedido2.setIdCliente(1);
        pedido3.setId(3);
        pedido3.setActual(true);
        pedido3.setEstado(2);
        pedido3.setTotal(BigDecimal.valueOf(300));
        pedido3.setIdCliente(1);
        List<Pedido> pedidosEmpleado = new ArrayList<>();
        pedidosEmpleado.add(pedido1);
        pedidosEmpleado.add(pedido2);
        pedidosEmpleado.add(pedido3);
        Usuario usuario = new Usuario();
        usuario.setId(0);
        usuario.setIdRol(2);

        //Act
        doThrow(IllegalArgumentException.class).when(validaciones).validarIDCliente(0);

        //Assert
        assertThrows(IllegalArgumentException.class, () -> pedidoService.consultarPedidosActuales(0));
        verify(validaciones, times(1)).validarIDCliente(0);
    }

    @Test
    void consultarPedidosActuales_NoHayPedidos(){
        //Arrange
        Pedido pedido1 = new Pedido();
        Pedido pedido2 = new Pedido();
        Pedido pedido3 = new Pedido();
        pedido1.setId(1);
        pedido1.setActual(true);
        pedido1.setEstado(4);
        pedido1.setTotal(BigDecimal.valueOf(100));
        pedido1.setIdCliente(1);
        pedido2.setId(2);
        pedido2.setActual(true);
        pedido2.setEstado(3);
        pedido2.setTotal(BigDecimal.valueOf(200));
        pedido2.setIdCliente(1);
        pedido3.setId(3);
        pedido3.setActual(true);
        pedido3.setEstado(1);
        pedido3.setTotal(BigDecimal.valueOf(300));
        pedido3.setIdCliente(1);
        List<Pedido> pedidosEmpleado = new ArrayList<>();
        pedidosEmpleado.add(pedido1);
        pedidosEmpleado.add(pedido2);
        pedidosEmpleado.add(pedido3);
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setIdRol(2);

        //Act
        doNothing().when(validaciones).validarIDCliente(1);
        when(pedidoRepository.findByIdClienteAndEstado(1,2)).thenReturn(null);

        //Assert
        assertThrows(NoSuchElementException.class, () -> pedidoService.consultarPedidosActuales(1));
    }

    @Test
    void crearPedidoCliente_Exito(){
        //Arrange
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setIdRol(3);

        //Act
        doNothing().when(validaciones).validarIDCliente(1);
        pedidoService.crearPedidoCliente(1);

        //Assert
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    void crearPedidoCliente_IDInvalido(){
        //Arrange
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setIdRol(3);

        //Act
        doThrow(IllegalArgumentException.class).when(validaciones).validarIDCliente(1);

        //Assert
        assertThrows(IllegalArgumentException.class, () -> pedidoService.crearPedidoCliente(1));
        verify(validaciones, times(1)).validarIDCliente(1);
    }

    @Test
    void crearPedidoEmpleado_Exito(){
        //Arrange
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setIdRol(2);

        doNothing().when(validaciones).validarIDCliente(1);


        //Act
        PedidoDTO respuesta =  pedidoService.crearPedidoEmpleado(1);

        //Assert
        assertEquals(BigDecimal.ZERO, respuesta.getTotal());
        verify(validaciones, times(1)).validarIDCliente(1);

    }

    @Test
    void crearPedidoEmpleado_IDInvalido(){
        //Arrange
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setIdRol(2);

        //Act
        doThrow(IllegalArgumentException.class).when(validaciones).validarIDCliente(0);

        //Assert
        assertThrows(IllegalArgumentException.class, () -> pedidoService.crearPedidoEmpleado(0));
        verify(validaciones, times(1)).validarIDCliente(0);
    }

    @Test
    void cambiarTotalPedido_Exito(){
        //Arrange
        Pedido pedidoBD = new Pedido();
        pedidoBD.setId(1);
        pedidoBD.setTotal(BigDecimal.valueOf(149.99));
        pedidoBD.setEstado(2);
        pedidoBD.setActual(true);
        pedidoBD.setIdCliente(1);
        BigDecimal nuevoTotal = BigDecimal.valueOf(200);

        doNothing().when(validaciones).validarIDPedido(1);
        when(pedidoRepository.getReferenceById(1)).thenReturn(pedidoBD);
        when(pedidoRepository.save(pedidoBD)).thenReturn(pedidoBD);
        doNothing().when(validaciones).validarPedido(any(Pedido.class));

        //Act
        PedidoDTO respuesta = pedidoService.cambiarTotalPedido(1, nuevoTotal);

        //Assert
        assertEquals(BigDecimal.valueOf(200), respuesta.getTotal());
        verify(validaciones, times(1)).validarIDPedido(1);
        verify(validaciones, times(1)).validarPedido(any(Pedido.class));
        verify(pedidoRepository, times(1)).getReferenceById(1);
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
    }

    @Test
    void cambiarTotalPedido_IDInvalido(){
        //Arrange
        Pedido pedidoSinCambio = new  Pedido();
        pedidoSinCambio.setId(1);
        pedidoSinCambio.setTotal(BigDecimal.valueOf(149.99));
        pedidoSinCambio.setEstado(2);
        pedidoSinCambio.setActual(true);
        pedidoSinCambio.setIdCliente(1);
        int id = 0;
        BigDecimal nuevoTotal = BigDecimal.valueOf(200);

        //Act
        doThrow(IllegalArgumentException.class).when(validaciones).validarIDPedido(id);

        //Assert
        assertThrows(IllegalArgumentException.class, () -> pedidoService.cambiarTotalPedido(id,  nuevoTotal));
        verify(validaciones, times(1)).validarIDPedido(id);
    }

    @Test
    void cambiarTotalPedido_PedidoNoExiste(){
        //Arrange
        int id = 1;
        BigDecimal nuevoTotal = BigDecimal.valueOf(200);


        //Act
        doNothing().when(validaciones).validarIDPedido(1);
        doThrow(NoSuchElementException.class).when(validaciones).validarPedido(null);

        //Assert
        assertThrows(NoSuchElementException.class, () -> pedidoService.cambiarTotalPedido(id,  nuevoTotal));
        verify(validaciones, times(1)).validarIDPedido(id);
        verify(pedidoRepository, times(1)).getReferenceById(id);
    }

    @Test
    void completarPedido_Exito(){
        //Arrange
        Pedido pedidoPagado = new Pedido();
        pedidoPagado.setId(1);
        pedidoPagado.setTotal(BigDecimal.valueOf(149.99));
        pedidoPagado.setEstado(2);
        pedidoPagado.setActual(true);
        pedidoPagado.setIdCliente(1);

        doNothing().when(validaciones).validarIDPedido(pedidoPagado.getId());
        when(pedidoRepository.getReferenceById(1)).thenReturn(pedidoPagado);
        doNothing().when(validaciones).validarPedido(any(Pedido.class));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedidoPagado);

        //Act
         pedidoService.cancelarPedido(1);

        //Assert
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
        verify(validaciones, times(1)).validarIDPedido(1);
        verify(validaciones, times(1)).validarPedido(any(Pedido.class));
    }

    @Test
    void completarPedido_IDInvalido(){
        //Arrange
        Pedido pedidoPagado = new Pedido();
        pedidoPagado.setId(0);
        pedidoPagado.setTotal(BigDecimal.valueOf(149.99));
        pedidoPagado.setEstado(2);
        pedidoPagado.setActual(true);
        pedidoPagado.setIdCliente(1);

        //Act
        doThrow(IllegalArgumentException.class).when(validaciones).validarIDPedido(pedidoPagado.getId());

        //Assert
        assertThrows(IllegalArgumentException.class, () -> pedidoService.completarPedido(pedidoPagado.getId()));
        verify(validaciones, times(1)).validarIDPedido(pedidoPagado.getId());

    }

    @Test
    void completarPedido_PedidoNoExiste(){
        //Arrange
        int id = 1;

        //Act
        doNothing().when(validaciones).validarIDPedido(id);
        doThrow(NoSuchElementException.class).when(validaciones).validarPedido(null);

        //Assert
        assertThrows(NoSuchElementException.class,() -> pedidoService.completarPedido(id));
        verify(validaciones, times(1)).validarIDPedido(id);
        verify(pedidoRepository, times(1)).getReferenceById(id);
    }

    @Test
    void eliminarPedido_Exito(){
        //Arrange
        Pedido pedidoEliminar = new Pedido();
        pedidoEliminar.setId(1);
        pedidoEliminar.setTotal(BigDecimal.valueOf(149.99));
        pedidoEliminar.setEstado(2);
        pedidoEliminar.setActual(true);
        pedidoEliminar.setIdCliente(1);

        doNothing().when(validaciones).validarIDPedido(pedidoEliminar.getId());
        when(pedidoRepository.getReferenceById(1)).thenReturn(pedidoEliminar);
        doNothing().when(validaciones).validarPedido(any(Pedido.class));

        //Act
        pedidoService.eliminarPedido(1);

        //Assert
        verify(pedidoRepository, times(1)).delete(any(Pedido.class));
        verify(validaciones, times(1)).validarIDPedido(1);
        verify(validaciones, times(1)).validarPedido(any(Pedido.class));
    }

    @Test
    void eliminarPedido_IDInvalido(){
        //Arrange
        Pedido pedidoEliminar = new Pedido();
        pedidoEliminar.setId(0);
        pedidoEliminar.setTotal(BigDecimal.valueOf(149.99));
        pedidoEliminar.setEstado(2);
        pedidoEliminar.setActual(true);
        pedidoEliminar.setIdCliente(1);

        //Act
        doThrow(IllegalArgumentException.class).when(validaciones).validarIDPedido(pedidoEliminar.getId());

        //Assert
        assertThrows(IllegalArgumentException.class,() -> pedidoService.eliminarPedido(0));
        verify(validaciones, times(1)).validarIDPedido(pedidoEliminar.getId());
    }

    @Test
    void eliminarPedido_PedidoNoExiste(){
        //Arrange
        int id = 1;

        //Act
        doNothing().when(validaciones).validarIDPedido(id);
        when(pedidoRepository.getReferenceById(id)).thenReturn(null);
        doThrow(NoSuchElementException.class).when(validaciones).validarPedido(null);

        //Assert
        assertThrows(NoSuchElementException.class, () ->  pedidoService.eliminarPedido(id));
    }

}
