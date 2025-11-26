package com.sweet_temptation.api.repository;

import com.sweet_temptation.api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario,Integer> {
    Optional<Usuario> findByCorreo(String correo);
    Optional<Usuario> findByUsuario(String usuario);
}
