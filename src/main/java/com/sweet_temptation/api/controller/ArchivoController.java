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

    // GUARDAR ARCHIVO
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

    // ASOCIAR ARCHIVO
    // ASOCIAR/ACTUALIZAR ARCHIVO
    @PutMapping("/asociar/{idArchivo}/{idProducto}") // <-- CAMBIO CLAVE
    public ResponseEntity<?> asociarArchivo(
            @PathVariable int idArchivo,
            @PathVariable int idProducto) {

        try {
            // En tu servicio, esta llamada DEBE borrar la asociación anterior si existe,
            // y luego crear la nueva. O simplemente actualizar el ID del archivo asociado
            // al producto dado.
            archivoService.asociarArchivo(idArchivo, idProducto);
            return ResponseEntity.status(HttpStatus.OK).build(); // Usar 200 OK para PUT exitoso
        } catch (IllegalArgumentException iae) {
            return ResponseEntity.badRequest().body(iae.getMessage());
        } catch (NoSuchElementException nsee) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(nsee.getMessage());
        }
    }

    // OBTENER DETALLES
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

    // OBTENER ARCHIVO POR ID
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

