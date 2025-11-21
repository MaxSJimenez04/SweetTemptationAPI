package com.sweet_temptation.api.controller;

import com.sweet_temptation.api.dto.PedidoDTO;
import com.sweet_temptation.api.servicios.EstadisticasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
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
}
