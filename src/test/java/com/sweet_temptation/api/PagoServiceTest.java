package com.sweet_temptation.api;

import com.sweet_temptation.api.dto.PagoRequestDTO;
import com.sweet_temptation.api.dto.PagoResponseDTO;
import com.sweet_temptation.api.model.Pago;
import com.sweet_temptation.api.model.Pedido;
import com.sweet_temptation.api.repository.PagoRepository;
import com.sweet_temptation.api.repository.PedidoRepository;
import com.sweet_temptation.api.servicios.PagoService;
import com.sweet_temptation.api.servicios.PedidoService;
import com.sweet_temptation.api.validaciones.PagoValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PagoServiceTest {

    @Mock
    private PagoRepository pagoRepository;

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private PedidoService pedidoService;

    @Mock
    private PagoValidator validaciones;

    @InjectMocks
    private PagoService pagoService;

    private final int ID_PEDIDO = 1;
    private final BigDecimal TOTAL_PEDIDO = BigDecimal.valueOf(100.00);
    private Pedido pedidoPendiente;
    private Pedido pedidoPagado;
    private Pago pagoExistente;

    @BeforeEach
    public void setUp() {
        pedidoPendiente = new Pedido();
        pedidoPendiente.setId(ID_PEDIDO);
        pedidoPendiente.setTotal(TOTAL_PEDIDO);
        pedidoPendiente.setEstado(2);
        pedidoPendiente.setActual(true);

        pedidoPagado = new Pedido();
        pedidoPagado.setId(ID_PEDIDO);
        pedidoPagado.setTotal(TOTAL_PEDIDO);
        pedidoPagado.setEstado(3);
        pedidoPagado.setActual(false);

        pagoExistente = new Pago();
        pagoExistente.setId(101);
        pagoExistente.setIdPedido(ID_PEDIDO);
        pagoExistente.setTotal(TOTAL_PEDIDO);
    }

    @Test
    void registrarPago_Efectivo_Exito_ConCambio() {
        BigDecimal montoEntregado = BigDecimal.valueOf(150.00);
        BigDecimal cambioEsperado = BigDecimal.valueOf(50.00).setScale(2, RoundingMode.HALF_UP);

        PagoRequestDTO request = new PagoRequestDTO();
        request.setTipoPago("Efectivo");
        request.setMontoPagado(montoEntregado);

        when(pedidoRepository.findById(ID_PEDIDO)).thenReturn(Optional.of(pedidoPendiente));
        when(pagoRepository.save(any(Pago.class))).thenReturn(pagoExistente);

        PagoResponseDTO respuesta = pagoService.registrarPago(ID_PEDIDO, request);

        assertEquals(pagoExistente.getId(), respuesta.getIdPago());
        assertEquals(cambioEsperado, respuesta.getCambioDevuelto());
        assertTrue(respuesta.getMensajeConfirmacion().contains("Cambio 50.00"));

        verify(validaciones, times(1)).validarPedidoPendientePago(pedidoPendiente);
        verify(pagoRepository, times(1)).save(any(Pago.class));
        verify(pedidoService, times(1)).completarPedido(ID_PEDIDO);
    }

    @Test
    void registrarPago_PedidoNoExiste_Falla() {
        PagoRequestDTO request = new PagoRequestDTO();
        request.setTipoPago("Efectivo");

        when(pedidoRepository.findById(ID_PEDIDO)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,
                () -> pagoService.registrarPago(ID_PEDIDO, request),
                "Debe lanzar NoSuchElementException si el pedido no existe.");

        verify(validaciones, times(1)).validarIDPedido(ID_PEDIDO);
        verify(pagoRepository, never()).save(any());
        verify(pedidoService, never()).completarPedido(anyInt());
    }

    @Test
    void registrarPago_PedidoYaPagado_Falla() {
        PagoRequestDTO request = new PagoRequestDTO();
        request.setTipoPago("Efectivo");
        request.setMontoPagado(TOTAL_PEDIDO);

        when(pedidoRepository.findById(ID_PEDIDO)).thenReturn(Optional.of(pedidoPagado));

        doThrow(new IllegalArgumentException("El pedido ya ha sido pagado."))
                .when(validaciones).validarPedidoPendientePago(pedidoPagado);

        assertThrows(IllegalArgumentException.class,
                () -> pagoService.registrarPago(ID_PEDIDO, request),
                "Debe fallar si se intenta pagar un pedido con estado completado/pagado.");

        verify(validaciones, times(1)).validarPedidoPendientePago(pedidoPagado);
        verify(pagoRepository, never()).save(any());
    }

    @Test
    void registrarPago_Efectivo_MontoInsuficiente_Falla() {
        BigDecimal montoInsuficiente = BigDecimal.valueOf(50.00);

        PagoRequestDTO request = new PagoRequestDTO();
        request.setTipoPago("Efectivo");
        request.setMontoPagado(montoInsuficiente);

        when(pedidoRepository.findById(ID_PEDIDO)).thenReturn(Optional.of(pedidoPendiente));

        assertThrows(IllegalArgumentException.class,
                () -> pagoService.registrarPago(ID_PEDIDO, request),
                "Debe fallar si el monto pagado es menor al total del pedido (FA02).");

        verify(validaciones, times(1)).validarPedidoPendientePago(pedidoPendiente);
        verify(pagoRepository, never()).save(any());
    }

    @Test
    void registrarPago_Tarjeta_DatosInvalidos_Falla() {
        PagoRequestDTO request = new PagoRequestDTO();
        request.setTipoPago("Tarjeta");
        request.setMontoPagado(TOTAL_PEDIDO);
        request.setDetallesCuenta(null);

        when(pedidoRepository.findById(ID_PEDIDO)).thenReturn(Optional.of(pedidoPendiente));

        doThrow(new IllegalArgumentException("Los detalles de la tarjeta son requeridos."))
                .when(validaciones).validarDatosTarjeta(null);

        assertThrows(IllegalArgumentException.class,
                () -> pagoService.registrarPago(ID_PEDIDO, request),
                "Debe fallar si se usa Tarjeta sin los detalles de cuenta.");

        verify(validaciones, times(1)).validarDatosTarjeta(null);
        verify(pagoRepository, never()).save(any());
    }
}