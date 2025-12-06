package com.sweet_temptation.api.repository;

import com.sweet_temptation.api.model.PedidoPersonalizado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PedidoPersonalizadoRepository extends JpaRepository<PedidoPersonalizado,Integer> {
}