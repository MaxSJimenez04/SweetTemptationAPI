package com.sweet_temptation.api.servicios;

import com.sweet_temptation.api.dto.EstadisticaProductoDTO;
import com.sweet_temptation.api.dto.EstadisticaVentaProductoDTO;
import com.sweet_temptation.api.dto.PedidoDTO;
import com.sweet_temptation.api.model.Pedido;
import com.sweet_temptation.api.repository.EstadisticasRepository;
import com.sweet_temptation.api.validaciones.EstadisticasValidator;
import com.sweet_temptation.api.validaciones.PedidoValidator;
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
    private final EstadisticasValidator validaciones;
    @PersistenceContext
    private EntityManager em;

    public EstadisticasService(EstadisticasRepository estadisticasRepository,EstadisticasValidator validaciones) {
        this.estadisticasRepository = estadisticasRepository;
        this.validaciones = validaciones;
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
        StoredProcedureQuery spq = em.createStoredProcedureQuery("dbo.sp_ProductosMasVendidos");
        spq.registerStoredProcedureParameter("FechaInicio", Date.class, ParameterMode.IN);
        spq.registerStoredProcedureParameter("FechaFin", Date.class, ParameterMode.IN);

        spq.setParameter("FechaInicio",java.sql.Date.valueOf(fechaInicio));
        spq.setParameter("FechaFin", java.sql.Date.valueOf(fechaFin));

        spq.execute();

        List<Object[]> rows = spq.getResultList();
        if (rows == null || rows.isEmpty()) {
            return null;
        }

        return rows.stream()
                .map(r -> new EstadisticaProductoDTO(
                        (String) r[0],
                        (String) r[1],
                        ((Number) r[2]).intValue()
                ))
                .toList();
    }

    public List<EstadisticaProductoDTO> consultarProductosImpopulares(LocalDate fechaInicio, LocalDate fechaFin){
        validaciones.validarLocalDate(fechaInicio, fechaFin);
        StoredProcedureQuery spq = em.createStoredProcedureQuery("dbo.sp_ProductosMenosVendidos");
        spq.registerStoredProcedureParameter("FechaInicio", Date.class, ParameterMode.IN);
        spq.registerStoredProcedureParameter("FechaFin", Date.class, ParameterMode.IN);

        spq.setParameter("FechaInicio",java.sql.Date.valueOf(fechaInicio));
        spq.setParameter("FechaFin", java.sql.Date.valueOf(fechaFin));

        spq.execute();

        List<Object[]> rows = spq.getResultList();

        if (rows == null || rows.isEmpty()) {
           return null;
        }

        return rows.stream()
                .map(r -> new EstadisticaProductoDTO(
                        (String) r[0],
                        (String) r[1],
                        ((Number) r[2]).intValue()
                ))
                .toList();
    }

    public List<EstadisticaVentaProductoDTO> obtenerVentasPorDia(LocalDate fechaInicio, LocalDate fechaFin,
                                                                 int idProducto){
        validaciones.validarLocalDate(fechaInicio, fechaFin);
        StoredProcedureQuery spq = em.createStoredProcedureQuery("dbo.sp_VentaProducto");

        spq.registerStoredProcedureParameter("FechaInicio", LocalDate.class, ParameterMode.IN);
        spq.registerStoredProcedureParameter("FechaFin", LocalDate.class, ParameterMode.IN);
        spq.registerStoredProcedureParameter("IdProducto", Integer.class, ParameterMode.IN);

        spq.setParameter("FechaInicio", fechaInicio);
        spq.setParameter("FechaFin", fechaFin);
        spq.setParameter("IdProducto", idProducto);

        spq.execute();

        List<Object[]> rows = spq.getResultList();

        if (rows.isEmpty()) {
            throw new NoSuchElementException("No se encontraron estadisticas en el rango");
        }
        return rows.stream()
                .map(r -> new EstadisticaVentaProductoDTO(
                        (Date) r[0],
                        ((Number) r[1]).intValue()
                ))
                .toList();
    }
}

