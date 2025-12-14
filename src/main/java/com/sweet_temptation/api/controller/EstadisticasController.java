package com.sweet_temptation.api.controller;

import com.sweet_temptation.api.dto.EstadisticaProductoDTO;
import com.sweet_temptation.api.dto.EstadisticaVentaProductoDTO;
import com.sweet_temptation.api.dto.PedidoDTO;
import com.sweet_temptation.api.servicios.EstadisticasService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequestMapping(path = "/estadisticas")
public class EstadisticasController {
    @Autowired
    private EstadisticasService estadisticasService;


    // ========== Estadisticas/reporte de ventas ==========

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

    @GetMapping("/ventas/descargarCSV")
    public void descargarReporteCSV(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            @RequestParam String estado,
            HttpServletResponse response) {

        String nombreArchivo = "reporte_ventas_" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ".csv";

        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + nombreArchivo + "\"");
        response.setCharacterEncoding("UTF-8");

        try (PrintWriter writer = response.getWriter()) {

            List<PedidoDTO> pedidosFiltrados = estadisticasService.consultarVentasPorRangoYEstado(fechaInicio, fechaFin, estado);

            writer.println("ID Rol,Tipo de Pedido,Fecha de Compra,Estado,Total (MXN)"); // Cambiamos Usuario por ID Rol

            for (PedidoDTO pedido : pedidosFiltrados) {

                String tipoUsuario = mapearRol(pedido.getIdRol());

                String tipoPedido = pedido.getPersonalizado() ? "Personalizado" : "Estandar";

                String estadoTexto = mapearEstado(pedido.getEstado());
                String fechaFormateada = pedido.getFechaCompra().toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                String totalFormateado = String.format("%.2f", pedido.getTotal().doubleValue());

                String fila = String.join(",",
                        tipoUsuario,
                        tipoPedido,
                        fechaFormateada,
                        estadoTexto,
                        totalFormateado);

                writer.println(fila);
            }
        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    private String mapearEstado(int codigoEstado) {
        if (codigoEstado == 3) return "Completada";
        if (codigoEstado == 4) return "Cancelada";
        if (codigoEstado == 2) return "Pendiente";
        return "Desconocido";
    }

    private String mapearRol(int idRol) {
        if (idRol == 1) return "Administrador";
        if (idRol == 2) return "Empleado";
        if (idRol == 3) return "Cliente";
        return "Error"; // Para manejar el caso 0 o cualquier otro valor inesperado.
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
