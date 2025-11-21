package com.sweet_temptation.api.repository;

import com.sweet_temptation.api.model.Pedido;

import java.time.LocalDateTime;
import java.util.List;

public interface EstadisticasRepository {
    // ========== Estadisticas de ventas ==========

    // Ventas por estado y rango de fechas
    List<Pedido> findByEstadoAndFechaCompra(
            LocalDateTime fechaInicio,
            LocalDateTime fechaFin,
            int estado
    );
}
