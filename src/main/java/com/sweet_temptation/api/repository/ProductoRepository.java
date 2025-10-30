package com.sweet_temptation.api.repository;

import com.sweet_temptation.api.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto,Integer> {
}
