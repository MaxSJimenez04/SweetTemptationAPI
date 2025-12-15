package com.sweet_temptation.api.controller;

import com.sweet_temptation.api.dto.DetallesProductoDTO;
import com.sweet_temptation.api.dto.PedidoDTO;
import com.sweet_temptation.api.dto.ProductoPedidoDTO;
import com.sweet_temptation.api.servicios.PedidoService;
import com.sweet_temptation.api.servicios.ProductoPedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequestMapping(path = "/pedido")
public class PedidoController {
    @Autowired
    private PedidoService pedidoService;
    @Autowired
    private ProductoPedidoService productoPedidoService;

    @GetMapping("/actual")
    public ResponseEntity<?> getActual(@RequestParam int idCliente){
        try{
            PedidoDTO pedidoActual = pedidoService.consultarPedidoActual(idCliente);
            return ResponseEntity.status(HttpStatus.OK).body(pedidoActual);
        } catch (IllegalArgumentException iae) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(iae.getMessage());
        }catch(NoSuchElementException nsee){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(nsee.getMessage());
        } catch (RuntimeException rte) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(rte.getMessage());
        }
    }

    @GetMapping(path = "/pedidos")
    public ResponseEntity<?> getActuales(@RequestParam int idCliente){
        try{
            List<PedidoDTO> listaPedidosActuales = pedidoService.consultarPedidosActuales(idCliente);
            return ResponseEntity.status(HttpStatus.OK).body(listaPedidosActuales);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (NoSuchElementException nsee){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(nsee.getMessage());
        } catch (RuntimeException rte) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(rte.getMessage());
        }
    }

    @PostMapping(path = "/")
    public ResponseEntity<?> crearPedidoEmpleado(@RequestParam int idEmpleado){
        try{
            PedidoDTO pedido = pedidoService.crearPedidoEmpleado(idEmpleado);
            return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
        } catch (IllegalArgumentException iae){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(iae.getMessage());
        } catch (NoSuchElementException nsee){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(nsee.getMessage());
        } catch (RuntimeException rte) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(rte.getMessage());
        }
    }

    @PostMapping(path = "/nuevo")
    public ResponseEntity<?> crearPedido(@RequestParam int idCliente){
        try{
            pedidoService.crearPedidoCliente(idCliente);
            return ResponseEntity.status(HttpStatus.CREATED).body(null);
        }catch (IllegalArgumentException iae){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(iae.getMessage());
        } catch (RuntimeException rte) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(rte.getMessage());
        }
    }

    @PutMapping (path = "/")
    public ResponseEntity<?> cancelarPedido(@RequestParam int idPedido){
        try{
            pedidoService.cancelarPedido(idPedido);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (NoSuchElementException nsee){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(nsee.getMessage());
        } catch (RuntimeException rte) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(rte.getMessage());
        }
    }

    @DeleteMapping(path = "/")
    public ResponseEntity<?> eliminarPedido(@RequestParam int idPedido){
        try {
            pedidoService.eliminarPedido(idPedido);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (NoSuchElementException nsee){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(nsee.getMessage());
        } catch (RuntimeException re) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(re.getMessage());
        }
    }

    //MÉTODOS PARA PRODUCTO PEDIDO

    @PostMapping(path = "{id}/")
    public ResponseEntity<?> crearProducto(@PathVariable int id, @RequestParam int idProducto,
        @RequestParam int idPedido, @RequestParam int cantidad){
            try{
                ProductoPedidoDTO respuesta = productoPedidoService.agregarProducto(idProducto, idPedido, cantidad);
                return ResponseEntity.status(HttpStatus.OK).body(respuesta);
            }catch (IllegalArgumentException iae){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(iae.getMessage());
            }catch(NoSuchElementException nsee){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(nsee.getMessage());
            }catch(RuntimeException re){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(re.getMessage());
            }
    }


    @PutMapping(path = "{id}/")
    public ResponseEntity<?> actualizarProducto(@PathVariable int id, @RequestBody ProductoPedidoDTO productoActualizado){
        try {
            ProductoPedidoDTO respuesta = productoPedidoService.actualizarProducto(productoActualizado.getId(),
                    productoActualizado.getCantidad());
            return ResponseEntity.status(HttpStatus.OK).body(respuesta);
        }catch(IllegalArgumentException iae){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(iae.getMessage());
        }catch (ArrayIndexOutOfBoundsException aoie) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(aoie.getMessage());
        }catch(RuntimeException re){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(re.getMessage());
        }
    }

    @PutMapping(path = "{id}/recalcular")
    public ResponseEntity<?> recalcularProducto(@PathVariable int id,@RequestParam int idPedido,
                                                @RequestBody BigDecimal cantidad){
        try{
            PedidoDTO pedidoActualizado = pedidoService.cambiarTotalPedido(idPedido,cantidad);
            return ResponseEntity.status(HttpStatus.OK).body(pedidoActualizado);
        }catch (IllegalArgumentException iae){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(iae.getMessage());
        }catch(NoSuchElementException nsee){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(nsee.getMessage());
        } catch (RuntimeException re){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(re.getMessage());
        }
    }

    @DeleteMapping(path = "{id}/")
    public ResponseEntity<?> eliminarProducto(@PathVariable int id, @RequestParam int idProducto) {
        try {
            productoPedidoService.eliminarProducto(idProducto);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }catch(IllegalArgumentException iae){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(iae.getMessage());
        } catch (RuntimeException re) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(re.getMessage());
        }
    }

    @GetMapping(path = "{id}/")
    public ResponseEntity<?> obtenerProductos(@PathVariable int id, @RequestParam int idPedido){
        try{
            List<DetallesProductoDTO> respuesta = productoPedidoService.obtenerListaProductos(idPedido);
            return ResponseEntity.status(HttpStatus.OK).body(respuesta);
        }  catch(IllegalArgumentException iae){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(iae.getMessage());
        }  catch (NoSuchElementException nsee){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(nsee.getMessage());
        }  catch (RuntimeException re){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(re.getMessage());
        }
    }

    @PutMapping(path = "{id}/comprar")
    public ResponseEntity<?> comprarProducto(@PathVariable int id, @RequestBody List<DetallesProductoDTO> productosCompra){
        try{
            productoPedidoService.comprarProductos(productosCompra);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }catch (IllegalArgumentException iae){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(iae.getMessage());
        }catch(RuntimeException re){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(re.getMessage());
        }
    }

    @GetMapping(path = "/consultar")
    public ResponseEntity<?> consultarPedidos(
            @RequestParam("fechaInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam("fechaFin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            @RequestParam("estado") String estado) {

        try {
            // Llama al nuevo método del servicio que implementamos antes
            List<PedidoDTO> pedidos = pedidoService.consultarPedidosConFiltros(fechaInicio, fechaFin, estado);

            if (pedidos.isEmpty()) {
                // Si no hay contenido, devuelve 204 No Content
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }

            // Si hay contenido, devuelve 200 OK con la lista de pedidos
            return ResponseEntity.status(HttpStatus.OK).body(pedidos);

        } catch (IllegalArgumentException iae) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(iae.getMessage());
        } catch (RuntimeException rte) {
            // Capturar errores generales (ej. de la DB)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(rte.getMessage());
        }
    }
}
