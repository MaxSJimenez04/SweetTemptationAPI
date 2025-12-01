package com.sweet_temptation.api;

import com.sweet_temptation.api.dto.DetallesProductoDTO;
import com.sweet_temptation.api.dto.PedidoDTO;
import com.sweet_temptation.api.grpc.TicketService;
import com.sweettemptation.grpc.TicketRequest;
import com.sweettemptation.grpc.TicketResponse;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceImplTest {

    @Mock
    private StreamObserver<TicketResponse> responseObserver;

    @InjectMocks
    @Spy   // Necesario para mockear métodos protected (spy)
    private TicketService service;

    @Test
    void generarTicket_Exito() {

        // Arrange
        int pedidoId = 10;

        TicketRequest request = TicketRequest.newBuilder()
                .setIdPedido(pedidoId)
                .build();
        List<DetallesProductoDTO> productos = List.of(
                new DetallesProductoDTO(1, 5, "Pastel de chocolate", BigDecimal.TEN, BigDecimal.valueOf(50.00),
                        1),
                new DetallesProductoDTO(2, 2, "Pastel de fresa", BigDecimal.valueOf(25.00), BigDecimal.valueOf(100.00),
                        2)
        );

        PedidoDTO pedido = new PedidoDTO(1, LocalDateTime.now(), true, BigDecimal.valueOf(100), 2,
                false, 1);
        pedido.setId(pedidoId);

        byte[] pdfBytes = "PDF_MOCK".getBytes();

        doReturn(productos).when(service).obtenerProductos(pedidoId);
        doReturn(pedido).when(service).obtenerDetallesPedido(pedidoId);
        doReturn(pdfBytes).when(service).crearEstructura(productos, pedido);

        // Act
        service.generarTicket(request, responseObserver);

        // Assert
        ArgumentCaptor<TicketResponse> captor = ArgumentCaptor.forClass(TicketResponse.class);

        verify(responseObserver).onNext(captor.capture());
        verify(responseObserver).onCompleted();

        TicketResponse resp = captor.getValue();

        assertEquals("ticket_pedido10.pdf", resp.getFileName());
        assertArrayEquals(pdfBytes, resp.getPdf().toByteArray());
    }

    @Test
    void generarTicket_IdInvalido() {

        // Arrange
        TicketRequest request = TicketRequest.newBuilder()
                .setIdPedido(0)     // inválido
                .build();

        // Act
        service.generarTicket(request, responseObserver);

        // Assert
        ArgumentCaptor<Throwable> captor = ArgumentCaptor.forClass(Throwable.class);

        verify(responseObserver).onError(captor.capture());

        Throwable error = captor.getValue();

        assertTrue(error instanceof StatusRuntimeException);

        StatusRuntimeException ex = (StatusRuntimeException) error;

        assertEquals(Status.INVALID_ARGUMENT.getCode(), ex.getStatus().getCode());
        assertEquals("El idPedido es requerido y debe ser mayor a 0",
                ex.getStatus().getDescription());

        verify(responseObserver, never()).onNext(any());
        verify(responseObserver, never()).onCompleted();
    }
}

