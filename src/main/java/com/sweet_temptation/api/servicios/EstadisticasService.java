package com.sweet_temptation.api.servicios;

import com.sweet_temptation.api.dto.PedidoDTO;
import com.sweet_temptation.api.model.Pedido;
import com.sweet_temptation.api.repository.EstadisticasRepository;
import com.sweet_temptation.api.validaciones.EstadisticasValidator;
import com.sweet_temptation.api.validaciones.PedidoValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class EstadisticasService {

    private final EstadisticasRepository estadisticasRepository;
    private final EstadisticasValidator validaciones;

    public EstadisticasService(EstadisticasRepository estadisticasRepository,EstadisticasValidator validaciones) {
        this.estadisticasRepository = estadisticasRepository;
        this.validaciones = validaciones;
    }

    @Transactional(readOnly = true)
    public List<PedidoDTO> consultarVentasPorRangoYEstado(
            LocalDate fechaInicio,
            LocalDate fechaFin,
            String estadoTexto
    ) {

        LocalDateTime inicioDateTime = fechaInicio.atStartOfDay();
        LocalDateTime finDateTime = fechaFin.atTime(LocalTime.MAX);

        // Validaciones
        validaciones.validarRangoFecha(inicioDateTime, finDateTime);
        int estado = validaciones.validarEstadoVenta(estadoTexto);

        // Consulta en la BD
        List<Pedido> pedidos = estadisticasRepository
                .findByEstadoAndFechaCompra(estado,inicioDateTime, finDateTime);

        if (pedidos == null || pedidos.isEmpty()) {
            throw new NoSuchElementException("No se encontraron ventas en el rango y estado indicados");
        }

        return pedidos.stream()
                .map(p -> new PedidoDTO(
                        p.getId(),
                        p.getFechaCompra(),
                        p.getActual(),
                        p.getTotal(),
                        p.getEstado(),
                        p.getPersonalizado(),
                        p.getIdCliente()
                )).toList();
    }
}

