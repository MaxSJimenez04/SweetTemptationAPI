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

@Controller
@RequestMapping(path = "/archivo")
public class ArchivoController {
    @Autowired
    private ArchivoService archivoService;

    @PostMapping(path = "/")
    public ResponseEntity<?> guardarArchivo(@RequestBody ArchivoDTO archivoDTO) {
        try{
           int idArchivo = archivoService.guardarArchivo(archivoDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(idArchivo);
        }catch (IllegalArgumentException iae){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(iae.getMessage());
        }catch (NoSuchElementException nsee){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(nsee.getMessage());
        }
    }

    @PostMapping(path = "/asociar")
    public ResponseEntity<?> asociarArchivo(@RequestParam int idArchivo, @RequestParam int idProducto){
        try{
            archivoService.asociarArchivo(idArchivo, idProducto);
            return ResponseEntity.status(HttpStatus.CREATED).body(null);
        }catch (IllegalArgumentException iae){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(iae.getMessage());
        }catch (NoSuchElementException nsee){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(nsee.getMessage());
        }
    }

    @GetMapping(path = "/")
    public ResponseEntity<?> obtenerDetallesArchivo(@RequestParam int idAsociacion){
        try{
            DetallesArchivoDTO detalles = archivoService.obtenerDatosArchivo(idAsociacion);
            return ResponseEntity.status(HttpStatus.CREATED).body(detalles);
        }catch (IllegalArgumentException iae){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(iae.getMessage());
        }catch (NoSuchElementException nsee){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(nsee.getMessage());
        }
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> obtenerImagen(@PathVariable int id) {
        try {
            ArchivoDTO respuesta = archivoService.obtenerArchivo(id);
            return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
        }catch (IllegalArgumentException iae){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(iae.getMessage());
        }catch (NoSuchElementException nsee){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(nsee.getMessage());
        }
    }
}
