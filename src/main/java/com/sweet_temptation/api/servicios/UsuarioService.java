package com.sweet_temptation.api.servicios;

import com.sweet_temptation.api.dto.user.UserRequestDTO;
import com.sweet_temptation.api.dto.user.UserResponseDTO;
import com.sweet_temptation.api.model.Usuario;
import com.sweet_temptation.api.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<UserResponseDTO> getAllUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public UserResponseDTO getUsuarioById(int id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado con ID: " + id));
        return toResponseDTO(usuario);
    }

    public UserResponseDTO createUsuario(UserRequestDTO request) {
        if (usuarioRepository.findByUsuario(request.getUsuario()).isPresent()) {
            throw new IllegalArgumentException("El nombre de usuario ya existe");
        }
        if (usuarioRepository.findByCorreo(request.getCorreo()).isPresent()) {
            throw new IllegalArgumentException("El correo ya está registrado");
        }

        Usuario usuario = new Usuario();
        usuario.setUsuario(request.getUsuario());
        usuario.setNombre(request.getNombre());
        usuario.setApellidos(request.getApellidos());
        usuario.setCorreo(request.getCorreo());
        usuario.setContrasena(request.getContrasena());
        usuario.setDireccion(request.getDireccion());
        usuario.setTelefono(request.getTelefono());
        usuario.setIdRol(request.getIdRol());
        usuario.setFechaRegistro(LocalDateTime.now());

        Usuario saved = usuarioRepository.save(usuario);
        return toResponseDTO(saved);
    }

    public UserResponseDTO updateUsuario(int id, UserRequestDTO request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado con ID: " + id));

        if (request.getUsuario() != null && !request.getUsuario().equals(usuario.getUsuario())) {
            if (usuarioRepository.findByUsuario(request.getUsuario()).isPresent()) {
                throw new IllegalArgumentException("El nombre de usuario ya existe");
            }
            usuario.setUsuario(request.getUsuario());
        }

        if (request.getCorreo() != null && !request.getCorreo().equals(usuario.getCorreo())) {
            if (usuarioRepository.findByCorreo(request.getCorreo()).isPresent()) {
                throw new IllegalArgumentException("El correo ya está registrado");
            }
            usuario.setCorreo(request.getCorreo());
        }

        if (request.getNombre() != null) {
            usuario.setNombre(request.getNombre());
        }
        if (request.getApellidos() != null) {
            usuario.setApellidos(request.getApellidos());
        }
        if (request.getContrasena() != null && !request.getContrasena().isEmpty()) {
            usuario.setContrasena(request.getContrasena());
        }
        if (request.getDireccion() != null) {
            usuario.setDireccion(request.getDireccion());
        }
        if (request.getTelefono() != null) {
            usuario.setTelefono(request.getTelefono());
        }
        if (request.getIdRol() > 0) {
            usuario.setIdRol(request.getIdRol());
        }

        usuario.setFechaModificacion(LocalDateTime.now());

        Usuario updated = usuarioRepository.save(usuario);
        return toResponseDTO(updated);
    }

    public void deleteUsuario(int id) {
        if (!usuarioRepository.existsById(id)) {
            throw new NoSuchElementException("Usuario no encontrado con ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    private UserResponseDTO toResponseDTO(Usuario usuario) {
        return new UserResponseDTO(
                usuario.getId(),
                usuario.getUsuario(),
                usuario.getNombre(),
                usuario.getApellidos(),
                usuario.getCorreo(),
                usuario.getDireccion(),
                usuario.getTelefono(),
                usuario.getIdRol(),
                usuario.getFechaRegistro()
        );
    }
}
