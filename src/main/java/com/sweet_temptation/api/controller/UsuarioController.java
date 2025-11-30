package com.sweet_temptation.api.controller;

import com.sweet_temptation.api.dto.user.UserRequestDTO;
import com.sweet_temptation.api.dto.user.UserResponseDTO;
import com.sweet_temptation.api.servicios.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/usuarios")
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsuarios() {
        List<UserResponseDTO> usuarios = usuarioService.getAllUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUsuarioById(@PathVariable int id) {
        try {
            UserResponseDTO usuario = usuarioService.getUsuarioById(id);
            return ResponseEntity.ok(usuario);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createUsuario(@RequestBody UserRequestDTO request) {
        try {
            UserResponseDTO created = usuarioService.createUsuario(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUsuario(@PathVariable int id, @RequestBody UserRequestDTO request) {
        try {
            UserResponseDTO updated = usuarioService.updateUsuario(id, request);
            return ResponseEntity.ok(updated);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUsuario(@PathVariable int id) {
        try {
            usuarioService.deleteUsuario(id);
            return ResponseEntity.ok("Usuario eliminado correctamente");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
