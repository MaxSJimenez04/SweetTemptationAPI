package com.sweet_temptation.api.repository;

import com.sweet_temptation.api.dto.DetallesProductoDTO;
import com.sweet_temptation.api.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto,Integer> {

    @Query("SELECT pp.id AS id, pp.cantidad AS cantidad, p.nombre AS nombre, p.precio AS precio, pp.subtotal AS subtotal, p.id AS idProducto\n" +
            " FROM Producto p JOIN ProductoPedido pp ON p.id = pp.idProducto \n" +
            " WHERE pp.idPedido = :idPedido")
    List<DetallesProductoDTO> obtenerListaProductos(int idPedido);

}
