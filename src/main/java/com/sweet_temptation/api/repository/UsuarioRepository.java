package com.sweet_temptation.api.repository;

import com.sweet_temptation.api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario,Integer> {
    Optional<Usuario> findByCorreo(String correo);
    Optional<Usuario> findByUsuario(String usuario);

    @Query("SELECT u.idRol FROM Usuario u WHERE u.id = ?1")
    Optional<Integer> findIdRolByIdUsuario(int idUsuario);
}
