package com.sweet_temptation.api;

import com.sweet_temptation.api.dto.PedidoDTO;
import com.sweet_temptation.api.model.Pedido;
import com.sweet_temptation.api.repository.EstadisticasRepository;
import com.sweet_temptation.api.repository.PedidoRepository;
import com.sweet_temptation.api.repository.UsuarioRepository;
import com.sweet_temptation.api.servicios.EstadisticasService;
import com.sweet_temptation.api.servicios.PedidoService;
import com.sweet_temptation.api.validaciones.EstadisticasValidator;
import com.sweet_temptation.api.validaciones.PedidoValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cglib.core.Local;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class EstadisticaVentasTest {
    @Mock
    private EstadisticasRepository estadisticasRepository;

    @Mock
    private EstadisticasValidator validaciones;

    @InjectMocks
    private EstadisticasService estadisticasService;

    @Mock
    private UsuarioRepository usuarioRepository; // para el rol


    // Prueba: consultar todas las ventas
    @Test
    void consultarVentas_Exito() {
        LocalDate inicio = LocalDate.of(2025, 1, 1);
        LocalDate fin = LocalDate.of(2025, 1, 31);
        int idCliente = 10;
        int idRolEsperado = 3; // Cliente

        Pedido pedido = new Pedido();
        pedido.setId(1);
        pedido.setTotal(BigDecimal.valueOf(200));
        pedido.setEstado(3);
        pedido.setFechaCompra(LocalDateTime.now());
        pedido.setActual(false);
        pedido.setPersonalizado(false);
        pedido.setIdCliente(idCliente);

        doNothing().when(validaciones).validarRangoFecha(any(LocalDateTime.class), any(LocalDateTime.class));
        when(validaciones.validarEstadoVenta("completada")).thenReturn(3);

        when(estadisticasRepository.findByEstadoAndFechaCompra(
                eq(3),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).thenReturn(List.of(pedido));

        when(usuarioRepository.findIdRolByIdUsuario(idCliente)).thenReturn(Optional.of(idRolEsperado));

        List<PedidoDTO> resultado = estadisticasService.consultarVentasPorRangoYEstado(
                inicio, fin, "completada"
        );

        assertFalse(resultado.isEmpty());
        assertEquals(idRolEsperado, resultado.get(0).getIdRol()); // Verifica que el rol es 3
        verify(estadisticasRepository, times(1)).findByEstadoAndFechaCompra(eq(3), any(), any());
        verify(usuarioRepository, times(1)).findIdRolByIdUsuario(idCliente); // Verifica que se consultó el rol
    }

    // Prueba: consultar todas las ventas canceladas
    @Test
    void consultarVentas_EstadoTodas(){
        LocalDate inicio = LocalDate.of(2025,12,1);
        LocalDate fin = LocalDate.of(2025,12,11);
        String estado = ""; // cadena vacia para todas
        int idCliente = 5;

        Pedido pedido = new Pedido();
        pedido.setId(2);
        pedido.setEstado(4); // Cancelada
        pedido.setIdCliente(idCliente);

        doNothing().when(validaciones).validarRangoFecha(any(), any());
        when(validaciones.validarEstadoVenta(estado)).thenReturn(0);

        when(estadisticasRepository.findByFechaCompraBetween(
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).thenReturn(List.of(pedido));

        when(usuarioRepository.findIdRolByIdUsuario(idCliente)).thenReturn(Optional.of(3));

        List<PedidoDTO> resultado = estadisticasService.consultarVentasPorRangoYEstado(
                inicio, fin, estado
        );

        assertFalse(resultado.isEmpty());

        // para verificar el metodo correcto
        verify(estadisticasRepository, times(1)).findByFechaCompraBetween(any(), any());

        verify(estadisticasRepository, never()).findByEstadoAndFechaCompra(anyInt(), any(), any());
    }

    // Prueba: excepcion - sin resultados
    @Test
    void consultarVentas_SinResultados_Excepcion() {
        LocalDate inicio = LocalDate.of(2025, 1, 1);
        LocalDate fin = LocalDate.of(2025, 1, 31);
        String estado = "pendiente";

        doNothing().when(validaciones).validarRangoFecha(any(), any());
        when(validaciones.validarEstadoVenta(estado)).thenReturn(2);

        when(estadisticasRepository.findByEstadoAndFechaCompra(
                eq(2),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).thenReturn(Collections.emptyList());

        // excepcion 404
        assertThrows(NoSuchElementException.class, () -> {
            estadisticasService.consultarVentasPorRangoYEstado(inicio, fin, estado);
        });

        verify(estadisticasRepository, times(1)).findByEstadoAndFechaCompra(eq(2), any(), any());

       verify(usuarioRepository, never()).findIdRolByIdUsuario(anyInt());
    }

}
