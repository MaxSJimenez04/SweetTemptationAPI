package com.sweet_temptation.api.repository;

import com.sweet_temptation.api.model.ProductoPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductoPedidoRepository extends JpaRepository<ProductoPedido,Integer> {
}
