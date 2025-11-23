package com.sweet_temptation.api.repository;

import com.sweet_temptation.api.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido,Integer> {

    Pedido findByActualTrueAndIdCliente(int  id);

    List<Pedido> findByIdClienteAndEstado(int  idCliente, int estado);

}
