package com.sweet_temptation.api.controller;

import com.sweet_temptation.api.dto.SolicitudPersonalizadaRequestDTO;
import com.sweet_temptation.api.model.Pedido;
import com.sweet_temptation.api.model.PedidoPersonalizado;
import com.sweet_temptation.api.model.Usuario;
import com.sweet_temptation.api.repository.PedidoRepository;
import com.sweet_temptation.api.repository.UsuarioRepository;
import com.sweet_temptation.api.servicios.PedidoCustomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pedidos-personalizados")
@CrossOrigin(origins = "*")
public class PedidoCustomController {

    @Autowired
    private PedidoCustomService service;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping
    public ResponseEntity<PedidoPersonalizado> crear(
            @RequestBody SolicitudPersonalizadaRequestDTO dto,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        if (userDetails == null) {
            return ResponseEntity.status(401).build();
        }

        Optional<Usuario> optionalUsuario = usuarioRepository.findByUsuario(userDetails.getUsername());
        if (optionalUsuario.isEmpty()) {
            return ResponseEntity.status(404).build();
        }

        Usuario usuario = optionalUsuario.get();

        Pedido pedido = new Pedido();
        pedido.setFechaCompra(LocalDateTime.now());
        pedido.setActual(true);
        pedido.setPersonalizado(true);
        pedido.setEstado(0);
        pedido.setTotal(BigDecimal.ZERO);
        pedido.setIdCliente(usuario.getId());

        pedido = pedidoRepository.save(pedido);

        PedidoPersonalizado personalizado = new PedidoPersonalizado();
        personalizado.setPedido(pedido);
        personalizado.setTamano(dto.getTamano());
        personalizado.setSaborBizcocho(dto.getSaborBizcocho());
        personalizado.setRelleno(dto.getRelleno());
        personalizado.setCobertura(dto.getCobertura());
        personalizado.setEspecificaciones(dto.getEspecificaciones());
        personalizado.setTelefonoContacto(dto.getTelefonoContacto());
        personalizado.setImagenUrl(dto.getImagenUrl());
        personalizado.setFechaSolicitud(LocalDateTime.now());
        personalizado.setEstado(1);

        PedidoPersonalizado guardado = service.guardar(personalizado);
        return ResponseEntity.ok(guardado);
    }

    @GetMapping
    public List<PedidoPersonalizado> listar() {
        return service.listar();
    }

    @PutMapping("/{id}/aceptar")
    public ResponseEntity<Void> aceptar(@PathVariable int id) {
        service.aceptar(id);
        return ResponseEntity.noContent().build();
    }

}