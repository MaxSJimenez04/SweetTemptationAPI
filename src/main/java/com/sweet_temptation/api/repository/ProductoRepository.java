package com.sweet_temptation.api.repository;

import com.sweet_temptation.api.dto.DetallesProductoDTO;
import com.sweet_temptation.api.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto,Integer> {

    @Query("SELECT new com.sweet_temptation.api.dto.DetallesProductoDTO(pp.id, pp.cantidad, p.nombre, p.precio, pp.subtotal, p.id) FROM Producto p JOIN ProductoPedido pp ON p.id = pp.idProducto WHERE pp.idPedido = :idPedido")

    List<DetallesProductoDTO> obtenerListaProductos(@Param("idPedido") int idPedido);

    List<Producto> findByCategoria(Integer categoria);
    List<Producto> findByNombre(String nombre);
    //Optional<Producto> findByID(Integer idProducto);


}
