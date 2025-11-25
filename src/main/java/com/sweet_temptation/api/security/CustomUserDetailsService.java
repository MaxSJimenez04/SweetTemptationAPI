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
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con correo: " + correo));

        Rol rol = rolRepository.findById(usuario.getIdRol())
                .orElseThrow(() -> new UsernameNotFoundException("Rol no encontrado para el usuario"));

        return new User(
                usuario.getCorreo(),
                usuario.getContrasena(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + rol.getRol().toUpperCase()))
        );
    }

    public String getRolByCorreo(String correo) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con correo: " + correo));

        Rol rol = rolRepository.findById(usuario.getIdRol())
                .orElseThrow(() -> new UsernameNotFoundException("Rol no encontrado para el usuario"));

        return rol.getRol();
    }
}

