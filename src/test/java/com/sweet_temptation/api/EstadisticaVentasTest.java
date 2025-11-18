package com.sweet_temptation.api;

import com.sweet_temptation.api.dto.PedidoDTO;
import com.sweet_temptation.api.model.Pedido;
import com.sweet_temptation.api.repository.PedidoRepository;
import com.sweet_temptation.api.servicios.PedidoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.management.Query;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class EstadisticaVentasTest {
    @Mock
    private PedidoRepository pedidoRepository;

    @InjectMocks
    private PedidoService reporteVentasService;

    @Test
    void consultarVentas_Exito(){
        LocalDate inicio = LocalDate.of(2025,1,1);
        LocalDate fin = LocalDate.of(2025,1,31);

        Pedido pedido = new Pedido();
        pedido.setId(1);
        pedido.setTotal(BigDecimal.valueOf(200));
        pedido.setEstado(3);
        pedido.setFechaCompra(LocalDateTime.now());

        when(pedidoRepository.findByEstadoAndFechaCompra(
                any(), any(), eq(3)
        )).thenReturn(List.of(pedido));

        List<PedidoDTO> resultado = reporteVentasService.consultarVentasPorRangoYEstado(inicio, fin, "completada");

        assertEquals(1, resultado.size());
        assertEquals(1, resultado.get(0).getId());
    }

}
