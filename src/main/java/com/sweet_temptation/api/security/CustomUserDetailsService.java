package com.sweet_temptation.api.security;

import com.sweet_temptation.api.model.Rol;
import com.sweet_temptation.api.model.Usuario;
import com.sweet_temptation.api.repository.RolRepository;
import com.sweet_temptation.api.repository.UsuarioRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository, RolRepository rolRepository) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsuario(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        Rol rol = rolRepository.findById(usuario.getIdRol())
                .orElseThrow(() -> new UsernameNotFoundException("Rol no encontrado para el usuario"));

        return new User(
                usuario.getUsuario(),
                usuario.getContrasena(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + rol.getRol().toUpperCase()))
        );
    }

    public Usuario getUsuarioCompleto(String username) {
        return usuarioRepository.findByUsuario(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
    }

    public String getRolByUsuario(String username) {
        Usuario usuario = usuarioRepository.findByUsuario(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        Rol rol = rolRepository.findById(usuario.getIdRol())
                .orElseThrow(() -> new UsernameNotFoundException("Rol no encontrado para el usuario"));

        return rol.getRol();
    }
}

