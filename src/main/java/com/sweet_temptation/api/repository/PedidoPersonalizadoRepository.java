package com.sweet_temptation.api.repository;

import com.sweet_temptation.api.model.PedidoPersonalizado;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PedidoPersonalizadoRepository
        extends JpaRepository<PedidoPersonalizado, Integer> {

    List<PedidoPersonalizado> findByEstado(Integer estado);
}
