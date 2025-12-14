package com.sweet_temptation.api.servicios;

import com.sweet_temptation.api.dto.EstadisticaProductoDTO;
import com.sweet_temptation.api.dto.EstadisticaVentaProductoDTO;
import com.sweet_temptation.api.dto.PedidoDTO;
import com.sweet_temptation.api.model.Pedido;
import com.sweet_temptation.api.model.Usuario;
import com.sweet_temptation.api.repository.EstadisticasProductoRepository;
import com.sweet_temptation.api.repository.EstadisticasRepository;
import com.sweet_temptation.api.repository.UsuarioRepository;
import com.sweet_temptation.api.validaciones.EstadisticasValidator;
import com.sweet_temptation.api.validaciones.PedidoValidator;
import com.sweet_temptation.api.validaciones.ProductoValidator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class EstadisticasService {

    private final EstadisticasRepository estadisticasRepository;
    private final EstadisticasProductoRepository  estadisticasProductoRepository;
    private final EstadisticasValidator validaciones;
    private final ProductoValidator productoValidator;
    private final UsuarioRepository usuarioRepository;

    public EstadisticasService(EstadisticasRepository estadisticasRepository, EstadisticasProductoRepository estadisticasProductoRepository, EstadisticasValidator validaciones, ProductoValidator  productoValidator, UsuarioRepository usuarioRepository) {
        this.estadisticasRepository = estadisticasRepository;
        this.validaciones = validaciones;
        this.productoValidator = productoValidator;
        this.estadisticasProductoRepository = estadisticasProductoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public List<PedidoDTO> consultarVentasPorRangoYEstado(LocalDate fechaInicio, LocalDate fechaFin, String estadoTexto) {
        LocalDateTime inicioDateTime = fechaInicio.atStartOfDay();
        LocalDateTime finDateTime = fechaFin.atTime(LocalTime.MAX);

        validaciones.validarRangoFecha(inicioDateTime, finDateTime);
        int estado = validaciones.validarEstadoVenta(estadoTexto);

        List<Pedido> pedidos;

        if (estado == 0) {
            pedidos = estadisticasRepository.findByFechaCompraBetween(inicioDateTime, finDateTime);
        } else {
            pedidos = estadisticasRepository
                    .findByEstadoAndFechaCompra(estado, inicioDateTime, finDateTime);
        }

        if (pedidos == null || pedidos.isEmpty()) {
            throw new NoSuchElementException("No se encontraron ventas en el rango y estado indicados");
        }
        //Para verificar desde consola
        System.out.println("--- INICIANDO MAPEO DE PEDIDOS ---");

        return pedidos.stream()
                .map(p -> {
                    int idRolObtenido = 2;
                    int idClientePedido = p.getIdCliente();

                    if (idClientePedido > 0) {
                        Optional<Integer> idRolOpt = usuarioRepository.findIdRolByIdUsuario(idClientePedido);

                        System.out.println("Pedido ID: " + p.getId() + " | idCliente: " + idClientePedido + " | JPQL isPresent: " + idRolOpt.isPresent());

                        if (idRolOpt.isPresent()) {
                            idRolObtenido = idRolOpt.get();
                        } else {
                            idRolObtenido = 3;
                        }
                    }

                    System.out.println("Pedido ID: " + p.getId() + " | ID ROL FINAL ENVIADO: " + idRolObtenido);

                    return new PedidoDTO(
                            p.getId(),
                            p.getFechaCompra(),
                            p.getActual(),
                            p.getTotal(),
                            p.getEstado(),
                            p.getPersonalizado(),
                            idClientePedido,
                            idRolObtenido
                    );
                }).toList();
    }

    // MÉTODOS PARA ESTADÍSTICAS DE PRODUCTOS
    public List<EstadisticaProductoDTO> obtenerEstasticasProductos(LocalDate fechaInicio, LocalDate fechaFin) {
        validaciones.validarLocalDate(fechaInicio, fechaFin);
        List<EstadisticaProductoDTO> estadisticasProductos = new ArrayList<>();
        List<EstadisticaProductoDTO> mejoresVentas = consultarProductosPopulares(fechaInicio, fechaFin);
        List<EstadisticaProductoDTO> peoresVentas = consultarProductosImpopulares(fechaInicio, fechaFin);

        if (mejoresVentas == null  || peoresVentas == null) {
            throw new NoSuchElementException("No se encontraron estadisticas en el rango seleccionado");
        } else if (mejoresVentas.isEmpty() || peoresVentas.isEmpty()) {
            throw new NullPointerException("No se encontraron estadisticas seleccionadas");
        }

        for (EstadisticaProductoDTO estadisticaProducto : mejoresVentas) {
            estadisticasProductos.add(estadisticaProducto);
        }

        for  (EstadisticaProductoDTO estadisticaProducto : peoresVentas) {
            estadisticasProductos.add(estadisticaProducto);
        }

        return estadisticasProductos;
    }

    public List<EstadisticaProductoDTO> consultarProductosPopulares(LocalDate fechaInicio, LocalDate fechaFin) {
        validaciones.validarLocalDate(fechaInicio, fechaFin);
        List<EstadisticaProductoDTO> respuestaMejoresVentas = estadisticasProductoRepository.obtenerMejoresVentas(fechaInicio, fechaFin);
        return respuestaMejoresVentas;
    }

    public List<EstadisticaProductoDTO> consultarProductosImpopulares(LocalDate fechaInicio, LocalDate fechaFin){
        validaciones.validarLocalDate(fechaInicio, fechaFin);
        List<EstadisticaProductoDTO> respuestaPeoresVentas = estadisticasProductoRepository.obtenerPeoresVentas(fechaInicio, fechaFin);
        return respuestaPeoresVentas;
    }

    public List<EstadisticaVentaProductoDTO> obtenerVentasPorDia(LocalDate fechaInicio, LocalDate fechaFin,
                                                                 int idProducto) {
        validaciones.validarLocalDate(fechaInicio, fechaFin);
        productoValidator.validarIDProducto(idProducto);
        List<EstadisticaVentaProductoDTO> ventasPorDia = estadisticasProductoRepository.obtenerVentasPorDia(fechaInicio, fechaFin, idProducto);
        if (ventasPorDia == null) {
            throw new NoSuchElementException("No se encontraron estadisticas seleccionadas");
        }
        return ventasPorDia;
    }
}

