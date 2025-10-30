package com.sweet_temptation.api.repository;

import com.sweet_temptation.api.model.PedidoPersonalizado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<PedidoPersonalizado,Integer> {
}
