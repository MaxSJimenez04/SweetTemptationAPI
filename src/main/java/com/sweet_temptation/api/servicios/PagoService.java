package com.sweet_temptation.api.servicios;

import com.sweet_temptation.api.dto.PagoDTO;
import com.sweet_temptation.api.dto.PagoRequestDTO;
import com.sweet_temptation.api.dto.PagoResponseDTO;
import com.sweet_temptation.api.model.Pago;
import com.sweet_temptation.api.model.Pedido;
import com.sweet_temptation.api.repository.PagoRepository;
import com.sweet_temptation.api.repository.PedidoRepository;
import com.sweet_temptation.api.validaciones.PagoValidator;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class PagoService {
    private final PagoRepository pagoRepository;
    private final PedidoRepository pedidoRepository;
    private final PedidoService pedidoService;
    private final PagoValidator validaciones;

    public PagoService(PagoRepository pagoRepository, PedidoRepository pedidoRepository,
                       PedidoService pedidoService, PagoValidator validaciones) {
        this.pagoRepository = pagoRepository;
        this.pedidoRepository = pedidoRepository;
        this.pedidoService = pedidoService;
        this.validaciones = validaciones;
    }

    public PagoResponseDTO registrarPago(int idPedido, PagoRequestDTO requestDTO) {
        validaciones.validarIDPedido(idPedido);
        Optional<Pedido> pedidoOptional = pedidoRepository.findById(idPedido);

        if (pedidoOptional.isEmpty()) {
            throw new NoSuchElementException("Pedido con ID " + idPedido + " no encontrado.");
        }
        Pedido pedidoBD = pedidoOptional.get();

        validaciones.validarPedidoPendientePago(pedidoBD);

        BigDecimal totalPedido = pedidoBD.getTotal();
        BigDecimal montoPagado = requestDTO.getMontoPagado();
        String tipoPago = requestDTO.getTipoPago();
        BigDecimal cambio = BigDecimal.ZERO;
        String mensaje = "Pago exitoso";

        if ("Efectivo".equalsIgnoreCase(tipoPago)) {
            if (montoPagado.compareTo(totalPedido) < 0) {
                throw new IllegalArgumentException("Cantidad insuficiente. Total a pagar: " + totalPedido);
            }
            cambio = montoPagado.subtract(totalPedido).setScale(2, RoundingMode.HALF_UP);
            mensaje = "Efectivo " + montoPagado + ", Cambio " + cambio;
        } else if ("Tarjeta".equalsIgnoreCase(tipoPago)) {
            validaciones.validarDatosTarjeta(requestDTO.getDetallesCuenta());

            if (totalPedido.compareTo(BigDecimal.ZERO) > 0) {
                mensaje = "Transacción con Tarjeta exitosa.";
            } else {
                throw new IllegalArgumentException("El total del pedido no puede ser cero.");
            }
        } else {
            throw new IllegalArgumentException("Método de pago no válido: " + tipoPago);
        }

        Pago pagoNuevo = new Pago();
        pagoNuevo.setTotal(totalPedido);
        pagoNuevo.setFechaPago(LocalDateTime.now());
        pagoNuevo.setTipoPago(tipoPago);
        pagoNuevo.setCuenta(requestDTO.getDetallesCuenta() != null ? requestDTO.getDetallesCuenta() : "N/A");
        pagoNuevo.setIdPedido(idPedido);

        Pago pagoBD = pagoRepository.save(pagoNuevo);

        pedidoService.completarPedido(idPedido);

        PagoResponseDTO responseDTO = new PagoResponseDTO(pagoBD.getId(), mensaje, cambio, totalPedido);
        responseDTO.setIdPago(pagoBD.getId());
        responseDTO.setMensajeConfirmacion(mensaje);
        responseDTO.setCambioDevuelto(cambio);
        responseDTO.setTotalPagado(totalPedido);

        return responseDTO;
    }


    public PagoDTO consultarPagoPorPedido(int idPedido){
        validaciones.validarIDPedido(idPedido);
        Pago pagoBD = pagoRepository.findByIdPedido(idPedido);
        validaciones.validarPago(pagoBD);

        return new PagoDTO(pagoBD.getId(), pagoBD.getTotal(), pagoBD.getFechaPago(),
                pagoBD.getTipoPago(), pagoBD.getCuenta(), pagoBD.getIdPedido());
    }
}
