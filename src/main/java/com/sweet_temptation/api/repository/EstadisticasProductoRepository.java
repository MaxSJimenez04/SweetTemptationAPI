package com.sweet_temptation.api.repository;

import com.sweet_temptation.api.dto.EstadisticaProductoDTO;
import com.sweet_temptation.api.dto.EstadisticaVentaProductoDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Repository
public class EstadisticasProductoRepository {
    @PersistenceContext
    private EntityManager em;

    public List<EstadisticaProductoDTO> obtenerMejoresVentas(LocalDate fechaInicio, LocalDate fechaFin) {
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

    public List<EstadisticaProductoDTO> obtenerPeoresVentas(LocalDate fechaInicio, LocalDate fechaFin) {
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

    public List<EstadisticaVentaProductoDTO> obtenerVentasPorDia(LocalDate fechaInicio, LocalDate fechaFin, int idProducto) {
        StoredProcedureQuery spq = em.createStoredProcedureQuery("dbo.sp_VentaProducto");

        spq.registerStoredProcedureParameter("FechaInicio", LocalDate.class, ParameterMode.IN);
        spq.registerStoredProcedureParameter("FechaFin", LocalDate.class, ParameterMode.IN);
        spq.registerStoredProcedureParameter("IdProducto", Integer.class, ParameterMode.IN);

        spq.setParameter("FechaInicio", fechaInicio);
        spq.setParameter("FechaFin", fechaFin);
        spq.setParameter("IdProducto", idProducto);

        spq.execute();

        List<Object[]> rows = spq.getResultList();

        return rows.stream()
                .map(r -> new EstadisticaVentaProductoDTO(
                        (Date) r[0],
                        ((Number) r[1]).intValue()
                ))
                .toList();
    }
}
