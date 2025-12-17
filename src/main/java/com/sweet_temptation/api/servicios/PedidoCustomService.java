package com.sweet_temptation.api.servicios;

import com.sweet_temptation.api.model.PedidoPersonalizado;
import com.sweet_temptation.api.repository.PedidoPersonalizadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PedidoCustomService {

    @Autowired
    private PedidoPersonalizadoRepository repository;

    public PedidoPersonalizado guardar(PedidoPersonalizado pedido) {
        pedido.setFechaSolicitud(LocalDateTime.now());
        pedido.setEstado(0); // 0 = creado
        return repository.save(pedido);
    }

    public List<PedidoPersonalizado> listar() {
        return repository.findAll();
    }

    public List<PedidoPersonalizado> listarPendientes() {
        return repository.findByEstado(0);
    }

    public void marcarEnRevision(int id) {
        PedidoPersonalizado pedido = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        pedido.setEstado(2); // 2 = en revisión
        repository.save(pedido);
    }

    public void aceptar(int id) {
        PedidoPersonalizado pedido = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        pedido.setEstado(3); // 3 = aceptado
        repository.save(pedido);
    }

    public void rechazar(int id) {
        PedidoPersonalizado pedido = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        pedido.setEstado(4); // 4 = rechazado
        repository.save(pedido);
    }
}