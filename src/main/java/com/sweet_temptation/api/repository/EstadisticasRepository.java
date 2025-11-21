package com.sweet_temptation.api.repository;

import com.sweet_temptation.api.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EstadisticasRepository extends JpaRepository<Pedido, Integer> {

    // Ventas por estado y rango de fechas
    @Query("SELECT p FROM Pedido p " +
            "WHERE p.estado = :estado " +
            "AND p.fechaCompra BETWEEN :inicio AND :fin")
    List<Pedido> findByEstadoAndFechaCompra(
            @Param("estado") int estado,
            @Param("inicio") LocalDateTime fechaInicio,
            @Param("fin") LocalDateTime fechaFin
    );
}
