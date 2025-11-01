package com.sweet_temptation.api.servicios;

import com.sweet_temptation.api.dto.PedidoDTO;
import com.sweet_temptation.api.model.Pedido;
import com.sweet_temptation.api.repository.PedidoRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PedidoService {
    private final PedidoRepository pedidoRepository;


    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    //Consultar pedido actual del cliente
    public PedidoDTO consultarPedidoActual(int idCliente) {
        Pedido pedidoBD = pedidoRepository.findByActualTrueAndIdCliente(idCliente);
        if( pedidoBD == null){
            throw new RuntimeException("Pedido no encontrado");
        }
        return new PedidoDTO(pedidoBD.getId(), pedidoBD.getFechaCompra(), pedidoBD.getActual(),
                pedidoBD.getTotal(), pedidoBD.getEstado(), pedidoBD.getPersonalizado(), pedidoBD.getIdCliente());
    }

    //Consultar pedidos en curso del empleado
    public List<PedidoDTO> consultarPedidosActuales(int idCliente){
        List<Pedido> pedidosBD = pedidoRepository.findByIdClienteAndEstado(idCliente, 2);

        if(pedidosBD == null){
            throw new RuntimeException("No se encontraron pedidos");
        }

        List<PedidoDTO> pedidosActual = new ArrayList<>();

        for(Pedido pedido : pedidosBD){
            PedidoDTO pedidoActual = new PedidoDTO(pedido.getId(), pedido.getFechaCompra(), pedido.getActual(),
                    pedido.getTotal(), pedido.getEstado(), pedido.getPersonalizado(), pedido.getIdCliente());
            pedidosActual.add(pedidoActual);
        }
        return pedidosActual;
    }

    //TODO: Implementarlo en PagoCliente
    public void crearPedidoCliente(int idCliente){
        Pedido pedidoNuevo = new Pedido();
        pedidoNuevo.setIdCliente(idCliente);
        pedidoNuevo.setPersonalizado(false);
        pedidoNuevo.setEstado(2);
        pedidoRepository.save(pedidoNuevo);
    }

    public void crearPedidoEmpleado(int idEmpleado){
        Pedido pedidoNuevo = new Pedido();
        pedidoNuevo.setIdCliente(idEmpleado);
        pedidoNuevo.setPersonalizado(false);
        pedidoNuevo.setEstado(1);
        pedidoRepository.save(pedidoNuevo);
    }


    public PedidoDTO cambiarTotalPedido(Pedido pedido){
        Pedido pedidoBD = pedidoRepository.getReferenceById(pedido.getId());
        if(pedidoBD == null){
            throw new RuntimeException("Pedido no encontrado");
        }
        pedidoBD.setTotal(pedido.getTotal());
        return new PedidoDTO(pedidoBD.getId(), pedidoBD.getFechaCompra(), pedidoBD.getActual(),
        pedidoBD.getTotal(), pedidoBD.getEstado(), pedidoBD.getPersonalizado(), pedidoBD.getIdCliente());
    }


    public PedidoDTO cambiarEstadoPedido(Pedido pedido, int estado){
        Pedido pedidoBD = pedidoRepository.getReferenceById(pedido.getId());
        if(pedidoBD == null){
            throw new RuntimeException("Pedido no encontrado");
        }
        pedidoBD.setEstado(estado);
        pedidoRepository.save(pedidoBD);
        return new PedidoDTO(pedidoBD.getId(), pedidoBD.getFechaCompra(), pedidoBD.getActual(),
                pedidoBD.getTotal(), pedidoBD.getEstado(), pedidoBD.getPersonalizado(), pedidoBD.getIdCliente());
    }

    //TODO: Implementar al completar el pago
    public void completarPedido(Pedido pedidoPagado){
        Pedido pedidoBD = pedidoRepository.getReferenceById(pedidoPagado.getId());
        if(pedidoBD == null){
            throw new RuntimeException("Pedido no encontrado");
        }
        pedidoBD.setActual(pedidoPagado.getActual());
        pedidoBD.setEstado(3);
        pedidoBD.setFechaCompra(Date.from(Instant.now()));
        pedidoRepository.save(pedidoBD);
    }

    public void eliminarPedido(Pedido pedidoEliminar){
        Pedido pedidoBD = pedidoRepository.getReferenceById(pedidoEliminar.getId());
        if(pedidoBD == null){
            throw new RuntimeException("Pedido no encontrado");
        }
        pedidoRepository.delete(pedidoBD);
    }
}
