package com.sweet_temptation.api.controller;

import com.sweet_temptation.api.dto.PagoDTO; // Su DTO existente (para obtener historial)
import com.sweet_temptation.api.dto.PagoRequestDTO; // DTO para la entrada del pago
import com.sweet_temptation.api.dto.PagoResponseDTO; // DTO para la respuesta del pago
import com.sweet_temptation.api.servicios.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/pago")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @PostMapping(path = "/{idPedido}")
    public ResponseEntity<?> realizarPago(@PathVariable int idPedido, @RequestBody PagoRequestDTO pagoRequestDTO) {
        try {
            PagoResponseDTO respuesta = pagoService.registrarPago(idPedido, pagoRequestDTO);
            return ResponseEntity.status(HttpStatus.OK).body(respuesta);

        } catch (IllegalArgumentException iae) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(iae.getMessage());

        } catch (NoSuchElementException nsee) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(nsee.getMessage());

        } catch (RuntimeException re) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocurrió un error con el servidor: " + re.getMessage());
        }
    }

    @GetMapping(path = "/pedido/{idPedido}")
    public ResponseEntity<?> consultarPagoPorPedido(@PathVariable int idPedido) {
        try {
            PagoDTO pago = pagoService.consultarPagoPorPedido(idPedido);
            return ResponseEntity.status(HttpStatus.OK).body(pago);
        } catch (NoSuchElementException nsee) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(nsee.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al consultar el pago: " + e.getMessage());
        }
    }
}