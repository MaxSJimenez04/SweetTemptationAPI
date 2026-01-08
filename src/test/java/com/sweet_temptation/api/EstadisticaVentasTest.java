package com.sweet_temptation.api;

import com.sweet_temptation.api.dto.PedidoDTO;
import com.sweet_temptation.api.model.Pedido;
import com.sweet_temptation.api.repository.EstadisticasRepository;
import com.sweet_temptation.api.repository.UsuarioRepository;
import com.sweet_temptation.api.servicios.EstadisticasService;
import com.sweet_temptation.api.validaciones.EstadisticasValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private EstadisticasService estadisticasService;

    @Test
    void consultarVentas_Exito() {
        // Datos de entrada
        LocalDate inicio = LocalDate.of(2025, 1, 1);
        LocalDate fin = LocalDate.of(2025, 1, 31);
        String estadoTexto = "completada";
        int idCliente = 10;
        int idRolEsperado = 3;

        Pedido pedido = new Pedido();
        pedido.setId(1);
        pedido.setTotal(BigDecimal.valueOf(200));
        pedido.setEstado(3); // 3 = Completada
        pedido.setFechaCompra(LocalDateTime.now());
        pedido.setIdCliente(idCliente);

        doNothing().when(validaciones).validarRangoFecha(any(), any());
        when(validaciones.validarEstadoVenta(estadoTexto)).thenReturn(3);

        when(estadisticasRepository.findByEstadoAndFechaCompra(eq(3), any(), any()))
                .thenReturn(List.of(pedido));

        when(usuarioRepository.findIdRolByIdUsuario(idCliente)).thenReturn(Optional.of(idRolEsperado));

        List<PedidoDTO> resultado = estadisticasService.consultarVentasPorRangoYEstado(inicio, fin, estadoTexto);

        assertFalse(resultado.isEmpty());
        assertEquals(idRolEsperado, resultado.get(0).getIdRol());
        verify(estadisticasRepository, times(1)).findByEstadoAndFechaCompra(eq(3), any(), any());
    }

    @Test
    void consultarVentas_EstadoTodas() {
        // Datos de entrada
        LocalDate inicio = LocalDate.of(2025, 12, 1);
        LocalDate fin = LocalDate.of(2025, 12, 11);
        String estadoTexto = "Todas"; // El valor que viene del Spinner
        int idCliente = 5;

        Pedido pedido = new Pedido();
        pedido.setId(2);
        pedido.setEstado(4); // Cancelada (válida para 'Todas')
        pedido.setIdCliente(idCliente);

        doNothing().when(validaciones).validarRangoFecha(any(), any());
        when(validaciones.validarEstadoVenta(estadoTexto)).thenReturn(0);

        when(estadisticasRepository.findByFechaCompraBetweenAndEstadosValidos(any(), any()))
                .thenReturn(List.of(pedido));

        when(usuarioRepository.findIdRolByIdUsuario(idCliente)).thenReturn(Optional.of(3));

        List<PedidoDTO> resultado = estadisticasService.consultarVentasPorRangoYEstado(inicio, fin, estadoTexto);

        assertFalse(resultado.isEmpty());
        verify(estadisticasRepository, times(1)).findByFechaCompraBetweenAndEstadosValidos(any(), any());

        verify(estadisticasRepository, never()).findByFechaCompraBetween(any(), any());
    }

    @Test
    void consultarVentas_SinResultados_Excepcion() {
        LocalDate inicio = LocalDate.of(2025, 1, 1);
        LocalDate fin = LocalDate.of(2025, 1, 31);
        String estadoTexto = "Todas";

        doNothing().when(validaciones).validarRangoFecha(any(), any());
        when(validaciones.validarEstadoVenta(estadoTexto)).thenReturn(0);

        when(estadisticasRepository.findByFechaCompraBetweenAndEstadosValidos(any(), any()))
                .thenReturn(Collections.emptyList());

        // Verificación de lanzamiento de excepción NoSuchElementException (404)
        assertThrows(NoSuchElementException.class, () -> {
            estadisticasService.consultarVentasPorRangoYEstado(inicio, fin, estadoTexto);
        });

        verify(usuarioRepository, never()).findIdRolByIdUsuario(anyInt());
    }
}