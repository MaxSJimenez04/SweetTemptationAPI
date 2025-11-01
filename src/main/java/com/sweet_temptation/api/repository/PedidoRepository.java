package com.sweet_temptation.api.repository;

import com.sweet_temptation.api.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido,Integer> {

    //Buscar el pedido actual del cliente
    Pedido findByActualTrueAndIdCliente(int  id);

    //Buscar los pedido en proceso del empleado
    List<Pedido> findByIdClienteAndEstado(int  idCliente, int estado);

}
