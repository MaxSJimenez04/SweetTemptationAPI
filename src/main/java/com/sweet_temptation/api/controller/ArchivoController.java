package com.sweet_temptation.api.controller;

import com.sweet_temptation.api.dto.ArchivoDTO;
import com.sweet_temptation.api.dto.DetallesArchivoDTO;
import com.sweet_temptation.api.servicios.ArchivoService;
import com.sweet_temptation.api.servicios.UsuarioService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/archivo")
public class ArchivoController {

    @Autowired
    private ArchivoService archivoService;

    @PostMapping
    public ResponseEntity<?> guardarArchivo(@RequestBody ArchivoDTO archivoDTO) {
        try {
            int idArchivo = archivoService.guardarArchivo(archivoDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(idArchivo);
        } catch (IllegalArgumentException iae) {
            return ResponseEntity.badRequest().body(iae.getMessage());
        } catch (NoSuchElementException nsee) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(nsee.getMessage());
        }
    }

    @PutMapping("/asociar/{idArchivo}/{idProducto}")
    public ResponseEntity<?> asociarArchivo(
            @PathVariable int idArchivo,
            @PathVariable int idProducto) {

        try {
            archivoService.asociarArchivo(idArchivo, idProducto);
            return ResponseEntity.status(HttpStatus.OK).build(); // Usar 200 OK para PUT exitoso
        } catch (IllegalArgumentException iae) {
            return ResponseEntity.badRequest().body(iae.getMessage());
        } catch (NoSuchElementException nsee) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(nsee.getMessage());
        }
    }

    @GetMapping("/detalle")
    public ResponseEntity<?> obtenerDetallesArchivo(@RequestParam int idProducto) {
        try {
            DetallesArchivoDTO detalles = archivoService.obtenerDatosArchivo(idProducto);
            return ResponseEntity.ok(detalles);
        } catch (IllegalArgumentException iae) {
            return ResponseEntity.badRequest().body(iae.getMessage());
        } catch (NoSuchElementException nsee) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(nsee.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerImagen(@PathVariable int id) {
        try {
            ArchivoDTO respuesta = archivoService.obtenerArchivo(id);
            return ResponseEntity.ok(respuesta);
        } catch (IllegalArgumentException iae) {
            return ResponseEntity.badRequest().body(iae.getMessage());
        } catch (NoSuchElementException nsee) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(nsee.getMessage());
        }
    }
}

