package com.sweet_temptation.api.servicios;

import com.sweet_temptation.api.dto.EstadisticaProductoDTO;
import com.sweet_temptation.api.dto.EstadisticaVentaProductoDTO;
import com.sweet_temptation.api.dto.PedidoDTO;
import com.sweet_temptation.api.model.Pedido;
import com.sweet_temptation.api.repository.EstadisticasProductoRepository;
import com.sweet_temptation.api.repository.EstadisticasRepository;
import com.sweet_temptation.api.validaciones.EstadisticasValidator;
import com.sweet_temptation.api.validaciones.PedidoValidator;
import com.sweet_temptation.api.validaciones.ProductoValidator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class EstadisticasService {

    private final EstadisticasRepository estadisticasRepository;
    private final EstadisticasProductoRepository  estadisticasProductoRepository;
    private final EstadisticasValidator validaciones;
    private final ProductoValidator productoValidator;



    public EstadisticasService(EstadisticasRepository estadisticasRepository,
                               EstadisticasProductoRepository estadisticasProductoRepository,
                               EstadisticasValidator validaciones, ProductoValidator  productoValidator) {
        this.estadisticasRepository = estadisticasRepository;
        this.validaciones = validaciones;
        this.productoValidator = productoValidator;
        this.estadisticasProductoRepository = estadisticasProductoRepository;
    }

    @Transactional(readOnly = true)
    public List<PedidoDTO> consultarVentasPorRangoYEstado(
            LocalDate fechaInicio,
            LocalDate fechaFin,
            String estadoTexto
    ) {

        LocalDateTime inicioDateTime = fechaInicio.atStartOfDay();
        LocalDateTime finDateTime = fechaFin.atTime(LocalTime.MAX);

        // Validaciones
        validaciones.validarRangoFecha(inicioDateTime, finDateTime);
        int estado = validaciones.validarEstadoVenta(estadoTexto);

        // Consulta en la BD
        List<Pedido> pedidos = estadisticasRepository
                .findByEstadoAndFechaCompra(estado,inicioDateTime, finDateTime);

        if (pedidos == null || pedidos.isEmpty()) {
            throw new NoSuchElementException("No se encontraron ventas en el rango y estado indicados");
        }

        return pedidos.stream()
                .map(p -> new PedidoDTO(
                        p.getId(),
                        p.getFechaCompra(),
                        p.getActual(),
                        p.getTotal(),
                        p.getEstado(),
                        p.getPersonalizado(),
                        p.getIdCliente()
                )).toList();
    }

    // MÉTODOS PARA ESTADÍSTICAS DE PRODUCTOS
    public List<EstadisticaProductoDTO> obtenerEstasticasProductos(LocalDate fechaInicio, LocalDate fechaFin) {
        validaciones.validarLocalDate(fechaInicio, fechaFin);
        List<EstadisticaProductoDTO> estadisticasProductos = new ArrayList<>();
        List<EstadisticaProductoDTO> mejoresVentas = consultarProductosPopulares(fechaInicio, fechaFin);
        List<EstadisticaProductoDTO> peoresVentas = consultarProductosImpopulares(fechaInicio, fechaFin);

        if (mejoresVentas == null  || peoresVentas == null) {
            throw new NoSuchElementException("No se encontraron estadisticas en el rango seleccionado");
        } else if (mejoresVentas.isEmpty() || peoresVentas.isEmpty()) {
            throw new NullPointerException("No se encontraron estadisticas seleccionadas");
        }

        for (EstadisticaProductoDTO estadisticaProducto : mejoresVentas) {
            estadisticasProductos.add(estadisticaProducto);
        }

        for  (EstadisticaProductoDTO estadisticaProducto : peoresVentas) {
            estadisticasProductos.add(estadisticaProducto);
        }

        return estadisticasProductos;
    }

    public List<EstadisticaProductoDTO> consultarProductosPopulares(LocalDate fechaInicio, LocalDate fechaFin) {
        validaciones.validarLocalDate(fechaInicio, fechaFin);
        List<EstadisticaProductoDTO> respuestaMejoresVentas = estadisticasProductoRepository.obtenerMejoresVentas(fechaInicio, fechaFin);
        return respuestaMejoresVentas;
    }

    public List<EstadisticaProductoDTO> consultarProductosImpopulares(LocalDate fechaInicio, LocalDate fechaFin){
        validaciones.validarLocalDate(fechaInicio, fechaFin);
        List<EstadisticaProductoDTO> respuestaPeoresVentas = estadisticasProductoRepository.obtenerPeoresVentas(fechaInicio, fechaFin);
        return respuestaPeoresVentas;
    }

    public List<EstadisticaVentaProductoDTO> obtenerVentasPorDia(LocalDate fechaInicio, LocalDate fechaFin,
                                                                 int idProducto) {
        validaciones.validarLocalDate(fechaInicio, fechaFin);
        productoValidator.validarIDProducto(idProducto);
        List<EstadisticaVentaProductoDTO> ventasPorDia = estadisticasProductoRepository.obtenerVentasPorDia(fechaInicio, fechaFin, idProducto);
        if (ventasPorDia == null) {
            throw new NoSuchElementException("No se encontraron estadisticas seleccionadas");
        }
        return ventasPorDia;
    }
}

