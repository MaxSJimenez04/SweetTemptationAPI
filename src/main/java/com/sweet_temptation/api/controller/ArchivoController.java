package com.sweet_temptation.api.controller;

import com.sweet_temptation.api.dto.ArchivoDTO;
import com.sweet_temptation.api.dto.DetallesArchivoDTO;
import com.sweet_temptation.api.servicios.ArchivoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        } catch (RuntimeException rte) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(rte.getMessage());
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
        } catch (RuntimeException re) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(re.getMessage());
        }
    }

    @GetMapping("/detalle")
    public ResponseEntity<?> obtenerDetallesArchivo(@RequestParam int idProducto) {
        try {
            DetallesArchivoDTO detalles = archivoService.obtenerDatosArchivo(idProducto);
            return ResponseEntity.status(HttpStatus.OK).body(detalles);
        } catch (IllegalArgumentException iae) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(iae.getMessage());
        } catch (NoSuchElementException nsee) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(nsee.getMessage());
        }  catch (RuntimeException re) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(re.getMessage());
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
        }  catch (RuntimeException re) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(re.getMessage());
        }
    }
}

