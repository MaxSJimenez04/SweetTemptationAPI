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
    public ResponseEntity<?> getActual(@RequestParam int idCliente){
        try{
            PedidoDTO pedidoActual = pedidoService.consultarPedidoActual(idCliente);
            return ResponseEntity.status(HttpStatus.OK).body(pedidoActual);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping(path = "/pedidos")
    public ResponseEntity<?> getActuales(@RequestParam int idCliente){
        try{
            List<PedidoDTO> listaPedidosActuales = pedidoService.consultarPedidosActuales(idCliente);
            return ResponseEntity.status(HttpStatus.OK).body(listaPedidosActuales);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping(path = "/")
    public ResponseEntity<?> crearPedidoEmpleado(@RequestParam int idEmpleado){
        pedidoService.crearPedidoEmpleado(idEmpleado);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @PostMapping(path = "/nuevo")
    public ResponseEntity<?> crearPedido(@RequestParam int idCliente){
        pedidoService.crearPedidoCliente(idCliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @PutMapping (path = "/")
    public ResponseEntity<?> cancelarPedido(@RequestParam int idPedido, @RequestParam int idCliente){
        try{
            pedidoService.cancelarPedido(idPedido, idCliente);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @DeleteMapping(path = "/")
    public ResponseEntity<?> eliminarPedido(@PathVariable int idPedido){
        try {
            pedidoService.eliminarPedido(idPedido);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}
