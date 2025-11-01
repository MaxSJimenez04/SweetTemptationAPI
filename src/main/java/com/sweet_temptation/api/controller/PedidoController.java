package com.sweet_temptation.api.controller;

import com.sweet_temptation.api.dto.PedidoDTO;
import com.sweet_temptation.api.model.Pedido;
import com.sweet_temptation.api.servicios.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(path = "/pedido")
public class PedidoController {
    @Autowired
    private PedidoService pedidoService;

    @GetMapping("/actual")
    public ResponseEntity<?> getActual(@RequestBody int idcliente){
        try{
            PedidoDTO pedidoActual = pedidoService.consultarPedidoActual(idcliente);
            return ResponseEntity.status(HttpStatus.OK).body(pedidoActual);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping(path = "/pedidos")
    public ResponseEntity<?> getActuales(@RequestBody int idcliente){
        try{
            List<PedidoDTO> listaPedidosActuales = pedidoService.consultarPedidosActuales(idcliente);
            return ResponseEntity.status(HttpStatus.OK).body(listaPedidosActuales);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping(path = "/")
    public ResponseEntity<?> crearPedido(@RequestBody int idEmpleado){
        pedidoService.crearPedidoEmpleado(idEmpleado);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @PutMapping (path = "/")
    public ResponseEntity<?> cancelarPedido(@RequestBody Pedido pedidoCancelar){
        try{
            PedidoDTO pedidoActualizado = pedidoService.cambiarEstadoPedido(pedidoCancelar, 3);
            return ResponseEntity.status(HttpStatus.OK).body(pedidoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @DeleteMapping(path = "/")
    public ResponseEntity<?> eliminarPedido(@RequestBody Pedido pedidoEliminar){
        try {
            pedidoService.eliminarPedido(pedidoEliminar);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}
