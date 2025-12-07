package com.sweet_temptation.api.repository;

import com.sweet_temptation.api.model.PasswordHistory;
import com.sweet_temptation.api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PasswordHistoryRepository extends JpaRepository<PasswordHistory, Integer> {
    List<PasswordHistory> findByUsuarioOrderByFechaCreacionDesc(Usuario usuario);
}

