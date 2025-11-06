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
import java.util.NoSuchElementException;

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
        } catch (IllegalArgumentException iae) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(iae.getMessage());
        }catch(NoSuchElementException nsee){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(nsee.getMessage());
        }
    }

    @GetMapping(path = "/pedidos")
    public ResponseEntity<?> getActuales(@RequestParam int idCliente){
        try{
            List<PedidoDTO> listaPedidosActuales = pedidoService.consultarPedidosActuales(idCliente);
            return ResponseEntity.status(HttpStatus.OK).body(listaPedidosActuales);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (NoSuchElementException nsee){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(nsee.getMessage());
        }
    }

    @PostMapping(path = "/")
    public ResponseEntity<?> crearPedidoEmpleado(@RequestParam int idEmpleado){
        try{
            pedidoService.crearPedidoEmpleado(idEmpleado);
            return ResponseEntity.status(HttpStatus.CREATED).body(null);
        }catch (IllegalArgumentException iae){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(iae.getMessage());
        }

    }

    @PostMapping(path = "/nuevo")
    public ResponseEntity<?> crearPedido(@RequestParam int idCliente){
        try{
            pedidoService.crearPedidoCliente(idCliente);
            return ResponseEntity.status(HttpStatus.CREATED).body(null);
        }catch (IllegalArgumentException iae){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(iae.getMessage());
        }
    }

    @PutMapping (path = "/")
    public ResponseEntity<?> cancelarPedido(@RequestParam int idPedido, @RequestParam int idCliente){
        try{
            pedidoService.cancelarPedido(idPedido);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (NoSuchElementException nsee){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(nsee.getMessage());
        }
    }

    @DeleteMapping(path = "/")
    public ResponseEntity<?> eliminarPedido(@PathVariable int idPedido){
        try {
            pedidoService.eliminarPedido(idPedido);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (NoSuchElementException nsee){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(nsee.getMessage());
        }
    }
}
