package com.sweet_temptation.api;

import com.sweet_temptation.api.dto.PedidoDTO;
import com.sweet_temptation.api.model.Pedido;
import com.sweet_temptation.api.repository.EstadisticasRepository;
import com.sweet_temptation.api.repository.PedidoRepository;
import com.sweet_temptation.api.servicios.EstadisticasService;
import com.sweet_temptation.api.servicios.PedidoService;
import com.sweet_temptation.api.validaciones.EstadisticasValidator;
import com.sweet_temptation.api.validaciones.PedidoValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @Test
    void consultarVentas_Exito(){
        LocalDate inicio = LocalDate.of(2025,1,1);
        LocalDate fin = LocalDate.of(2025,1,31);

        Pedido pedido = new Pedido();
        pedido.setId(1);
        pedido.setTotal(BigDecimal.valueOf(200));
        pedido.setEstado(3);
        pedido.setFechaCompra(LocalDateTime.now());
        pedido.setActual(false);
        pedido.setPersonalizado(false);
        pedido.setIdCliente(10);

        doNothing().when(validaciones)
                .validarRangoFecha(any(LocalDateTime.class), any(LocalDateTime.class));

        when(validaciones.validarEstadoVenta("completada")).thenReturn(3);

        when(estadisticasRepository.findByEstadoAndFechaCompra(
                eq(3),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).thenReturn(List.of(pedido));

        List<PedidoDTO> resultado = estadisticasService.consultarVentasPorRangoYEstado(
                inicio, fin, "completada"
        );

        assertEquals(1, resultado.size());
        assertEquals(1, resultado.get(0).getId());
        assertEquals(BigDecimal.valueOf(200), resultado.get(0).getTotal());

        verify(validaciones, times(1)).validarRangoFecha(any(LocalDateTime.class), any(LocalDateTime.class));

        verify(validaciones, times(1)).validarEstadoVenta("completada");

        verify(estadisticasRepository, times(1)).findByEstadoAndFechaCompra(eq(3),any(LocalDateTime.class), any(LocalDateTime.class));
    }

}
