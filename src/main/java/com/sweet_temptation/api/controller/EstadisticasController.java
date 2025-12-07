package com.sweet_temptation.api.controller;

import com.sweet_temptation.api.dto.EstadisticaProductoDTO;
import com.sweet_temptation.api.dto.EstadisticaVentaProductoDTO;
import com.sweet_temptation.api.dto.PedidoDTO;
import com.sweet_temptation.api.servicios.EstadisticasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequestMapping(path = "/estadisticas")
public class EstadisticasController {
    @Autowired
    private EstadisticasService estadisticasService;


    // ========== Estadisticas de ventas ==========

    @GetMapping("/ventas")
    public ResponseEntity<?> consultarVentas(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fechaInicio,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fechaFin,

            @RequestParam String estado
    ){
        try{
            List<PedidoDTO> ventas = estadisticasService.consultarVentasPorRangoYEstado(fechaInicio, fechaFin, estado);

            return ResponseEntity.status(HttpStatus.OK).body(ventas);
        }catch(IllegalArgumentException ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }catch (NoSuchElementException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    // ========== Estadisticas Productos ==========
    @GetMapping(path = "/productos/")
    public ResponseEntity<?> consultarEstadisiticasProductos(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin){
        try {
            List<EstadisticaProductoDTO> estadisticas = estadisticasService.obtenerEstasticasProductos(fechaInicio, fechaFin);
            return ResponseEntity.status(HttpStatus.OK).body(estadisticas);
        }catch(IllegalArgumentException ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }catch(NoSuchElementException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }catch (NullPointerException ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }


    @GetMapping(path = "/productos/{id}")
    public ResponseEntity<?> consultarVentasProducto(
            @PathVariable int id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            @RequestParam int idProducto){
        try {
            List<EstadisticaVentaProductoDTO> estadistica =
                    estadisticasService.obtenerVentasPorDia(fechaInicio, fechaFin, idProducto);
            return ResponseEntity.status(HttpStatus.OK).body(estadistica);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (NoSuchElementException se) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(se.getMessage());
        }
    }
}
