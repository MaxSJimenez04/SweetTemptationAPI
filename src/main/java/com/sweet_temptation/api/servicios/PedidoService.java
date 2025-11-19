package com.sweet_temptation.api.servicios;

import com.sweet_temptation.api.dto.PedidoDTO;
import com.sweet_temptation.api.model.Pedido;
import com.sweet_temptation.api.repository.PedidoRepository;
import com.sweet_temptation.api.validaciones.PedidoValidator;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class PedidoService {
    private final PedidoRepository pedidoRepository;
    private final PedidoValidator validaciones;


    public PedidoService(PedidoRepository pedidoRepository,  PedidoValidator validaciones) {
        this.pedidoRepository = pedidoRepository;
        this.validaciones = validaciones;
    }

    //Consultar pedido actual del cliente
    public PedidoDTO consultarPedidoActual(int idCliente) {
        validaciones.validarIDCliente(idCliente);
        Pedido pedidoBD = pedidoRepository.findByActualTrueAndIdCliente(idCliente);
        validaciones.validarPedido(pedidoBD);
        return new PedidoDTO(pedidoBD.getId(), pedidoBD.getFechaCompra(), pedidoBD.getActual(),
                pedidoBD.getTotal(), pedidoBD.getEstado(), pedidoBD.getPersonalizado(), pedidoBD.getIdCliente());
    }

    //Consultar pedidos en curso del empleado
    public List<PedidoDTO> consultarPedidosActuales(int idCliente){
        validaciones.validarIDCliente(idCliente);
        List<Pedido> pedidosBD = pedidoRepository.findByIdClienteAndEstadoIn(idCliente, Arrays.asList(1,2));

        if(pedidosBD == null){
            throw new NoSuchElementException("No se encontraron pedidos");
        }

        List<PedidoDTO> pedidosActual = new ArrayList<>();

        for(Pedido pedido : pedidosBD){
            PedidoDTO pedidoActual = new PedidoDTO(pedido.getId(), pedido.getFechaCompra(), pedido.getActual(),
                    pedido.getTotal(), pedido.getEstado(), pedido.getPersonalizado(), pedido.getIdCliente());
            pedidosActual.add(pedidoActual);
        }
        return pedidosActual;
    }

    public void crearPedidoCliente(int idCliente){
        validaciones.validarIDCliente(idCliente);
        Pedido pedidoNuevo = new Pedido();
        pedidoNuevo.setIdCliente(idCliente);
        pedidoNuevo.setPersonalizado(false);
        pedidoNuevo.setActual(true);
        pedidoNuevo.setEstado(2);
        pedidoNuevo.setTotal(BigDecimal.ZERO);
        pedidoNuevo.setFechaCompra(LocalDateTime.now());
        pedidoRepository.save(pedidoNuevo);
    }

    public PedidoDTO crearPedidoEmpleado(int idEmpleado){
        Pedido pedidoNuevo = new Pedido();
        validaciones.validarIDCliente(idEmpleado);
        pedidoNuevo.setIdCliente(idEmpleado);
        pedidoNuevo.setPersonalizado(false);
        pedidoNuevo.setActual(true);
        pedidoNuevo.setEstado(1);
        pedidoNuevo.setTotal(BigDecimal.ZERO);
        pedidoNuevo.setFechaCompra(LocalDateTime.now());
        Pedido pedidoBD = pedidoRepository.save(pedidoNuevo);
        validaciones.validarPedido(pedidoBD);
        return new PedidoDTO(pedidoNuevo.getId(), pedidoNuevo.getFechaCompra(), pedidoNuevo.getActual(),
                pedidoNuevo.getTotal(), pedidoNuevo.getEstado(), pedidoNuevo.getPersonalizado(),
                pedidoNuevo.getIdCliente());
    }

    public PedidoDTO cambiarTotalPedido(int idPedido, BigDecimal total){
        validaciones.validarIDPedido(idPedido);
        Pedido pedidoBD = pedidoRepository.getReferenceById(idPedido);
        validaciones.validarPedido(pedidoBD);
        pedidoBD.setTotal(total);
        Pedido pedidoActualizado = pedidoRepository.save(pedidoBD);
        return new PedidoDTO(pedidoActualizado.getId(), pedidoActualizado.getFechaCompra(), pedidoActualizado.getActual(),
                pedidoActualizado.getTotal(), pedidoActualizado.getEstado(), pedidoActualizado.getPersonalizado(),
                pedidoActualizado.getIdCliente());
    }

    public void cancelarPedido(int idPedido){
        validaciones.validarIDPedido(idPedido);
        Pedido pedidoBD = pedidoRepository.getReferenceById(idPedido);
        validaciones.validarPedido(pedidoBD);
        pedidoBD.setEstado(4);
        pedidoBD.setActual(false);
        pedidoRepository.save(pedidoBD);
    }

    //TODO: Implementar al completar el pago
    public void completarPedido(int idPedidoPagado){
        validaciones.validarIDPedido(idPedidoPagado);
        Pedido pedidoBD = pedidoRepository.getReferenceById(idPedidoPagado);
        validaciones.validarPedido(pedidoBD);
        pedidoBD.setActual(false);
        pedidoBD.setEstado(3);
        pedidoBD.setFechaCompra(LocalDateTime.now());
        pedidoRepository.save(pedidoBD);
    }

    public void eliminarPedido(int idPedidoEliminar){
        validaciones.validarIDPedido(idPedidoEliminar);
        Pedido pedidoBD = pedidoRepository.getReferenceById(idPedidoEliminar);
        validaciones.validarPedido(pedidoBD);
        pedidoRepository.delete(pedidoBD);
    }
}