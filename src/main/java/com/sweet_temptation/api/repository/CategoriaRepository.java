package com.sweet_temptation.api.repository;

import com.sweet_temptation.api.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    // Spring Data JPA ya proporciona findAll(), que es lo que necesitamos.
    // Opcional: Si deseas un método de consulta específico, podrías agregarlo aquí.
}
