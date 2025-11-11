package com.sweet_temptation.api.repository;

import com.sweet_temptation.api.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto,Integer> {
    List<Producto> findByIDCategoria(Integer categoria);
    List<Producto> findByNombre(String nombre);
    //Optional<Producto> findByID(Integer idProducto);
}
