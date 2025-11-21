package com.sweet_temptation.api;

import com.sweet_temptation.api.dto.ProductoDTO;
import com.sweet_temptation.api.model.Producto;
import com.sweet_temptation.api.repository.ProductoRepository;
import com.sweet_temptation.api.servicios.ProductoService;
import com.sweet_temptation.api.validaciones.ProductoValidator;
import org.hibernate.sql.ast.tree.expression.CaseSimpleExpression;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private ProductoValidator validaciones;

    @InjectMocks
    private ProductoService productoService;

    // ----Para consultas de un Producto----
    @Test
    void consultarProducto_Exitoso(){
        int id = 1;
        Producto producto = productoEjemplo1(id);
        doNothing().when(validaciones).validarIDProducto(id);
        when(productoRepository.findById(id)).thenReturn(Optional.of(producto));

        ProductoDTO productoDTO = productoService.consultarProducto(id);

        assertEquals(id, productoDTO.getId());
        assertEquals("Brownie", productoDTO.getNombre());
        verify(validaciones, times(1)).validarIDProducto(id);
        verify(productoRepository, times(1)).findById(id);
        verify(validaciones, times(1)).validarProducto(producto);
    }

    @Test
    void consultarProducto_IDInvalido(){
        doThrow(IllegalArgumentException.class).when(validaciones).validarIDProducto(0);

        assertThrows(IllegalArgumentException.class, () -> productoService.consultarProducto(0));
        verify(validaciones, times(1)).validarIDProducto(0);
        verifyNoInteractions(productoRepository);
    }

    @Test
    void consultarProducto_NoExiste(){
        int id = 100;
        doNothing().when(validaciones).validarIDProducto(id);
        when(productoRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> productoService.consultarProducto(id));
        verify(productoRepository, times(1)).findById(id);
    }

    // ----Consultar Productos----
    @Test
    void consultarProductos_Exito(){
        Producto producto1 = productoEjemplo1(1);
        Producto producto2 = productoEjemplo2(2);
        when(productoRepository.findAll()).thenReturn(Arrays.asList(producto1,producto2));

        var lista = productoService.consultarProductos();

        assertEquals(2, lista.size());
        verify(productoRepository, times(1)).findAll();
    }

    @Test
    void consultarProductos_Vacio(){
        when(productoRepository.findAll()).thenReturn(Collections.emptyList());

        var lista = productoService.consultarProductos();

        assertTrue(lista.isEmpty());
        verify(productoRepository, times(1)).findAll();


    }

    // ----Consultar por categoria----
    @Test
    void consultarPorCategoria_Exito(){
        int cat = 2;
        Producto producto1 = productoEjemplo1(1); producto1.setIdCategoria(cat);
        Producto producto2 = productoEjemplo2(2); producto2.setIdCategoria(cat);

        when(productoRepository.findByCategoria(cat)).thenReturn(Arrays.asList(producto1, producto2));

        var lista = productoService.consultarPorCategoria(cat);

        assertEquals(2, lista.size());
        verify(productoRepository, times(1)).findByCategoria(cat);
    }

    @Test
    void consultarPorCategoria_SinResultado(){
        int cat = 200;
        when(productoRepository.findByCategoria(cat)).thenReturn(Collections.emptyList());

        assertThrows(NoSuchElementException.class, () -> productoService.consultarPorCategoria(cat));
        verify(productoRepository, times(1)).findByCategoria(cat);
    }

    @Test
    void crerProducto_Exito(){
        ProductoDTO productoNuevo = new ProductoDTO(0,"Brownie", "Chocolate", BigDecimal.valueOf(39.50), true,30, LocalDateTime.now(), LocalDateTime.now(),1);
        doNothing().when(validaciones).validarProductoNuevo(productoNuevo);

        Producto productoGuardado = productoEjemplo1(10);
        when(productoRepository.save(any(Producto.class))).thenReturn(productoGuardado);

        int productoDTO = productoService.crearProducto(productoNuevo);

        assertEquals(10, productoDTO);
        verify(validaciones, times(1)).validarProductoNuevo(productoNuevo);
        verify(productoRepository, times(1)).save(any(Producto.class));
        verify(validaciones, times(1)).validarProducto(productoGuardado);
    }

    @Test
    void crearProducto_DTOInvalido(){
        ProductoDTO nuevo = new ProductoDTO(0, "", "Descripcion test", BigDecimal.valueOf(-1), true, 10, LocalDateTime.now(), LocalDateTime.now(),2);
        doThrow(IllegalArgumentException.class).when(validaciones).validarProductoNuevo(nuevo);

        assertThrows(IllegalArgumentException.class, () -> productoService.crearProducto(nuevo));
        verify(validaciones, times(1)).validarProductoNuevo(nuevo);
        verifyNoMoreInteractions(productoRepository);
    }

    @Test
    void actualizarProducto_Exito(){
        int id = 5;
        Producto producto = productoEjemplo1(id);
        ProductoDTO cambios = new ProductoDTO(id, "Brownie Premium", "Con nuez",
                BigDecimal.valueOf(55), true, 25, producto.getFechaRegistro(), LocalDateTime.now(), 2);

        doNothing().when(validaciones).validarIDProducto(id);
        when(productoRepository.findById(id)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class))).thenReturn(producto); // devolver lo modificado

        ProductoDTO dto = productoService.actualizarProducto(id, cambios);

        assertEquals("Brownie Premium", dto.getNombre());
        assertEquals(25, dto.getUnidades());
        verify(validaciones, times(1)).validarIDProducto(id);
        verify(productoRepository, times(1)).findById(id);
        verify(productoRepository, times(1)).save(any(Producto.class));
    }

    @Test
    void actualizarProducto_NoExiste(){
        int id = 123;
        ProductoDTO cambios = new ProductoDTO(id, "X", "Y", BigDecimal.TEN, true, 1,
                LocalDateTime.now(), LocalDateTime.now(), 2);
        doNothing().when(validaciones).validarIDProducto(id);
        when(productoRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> productoService.actualizarProducto(id, cambios));
        verify(productoRepository, times(1)).findById(id);
    }

    @Test
    void eliminarProducto_Exito(){
        int id = 2;

        doNothing().when(validaciones).validarIDProducto(id);
        when(productoRepository.existsById(id)).thenReturn(true);

        // Act
        productoService.eliminarProducto(id);

        // Assert
        verify(validaciones, times(1)).validarIDProducto(id);
        verify(productoRepository, times(1)).existsById(id);
        verify(productoRepository, times(1)).deleteById(id);
    }

    @Test
    void eliminarProducto_NoExiste(){
        int id = 2;

        doNothing().when(validaciones).validarIDProducto(id);
        when(productoRepository.existsById(id)).thenReturn(false);

        assertThrows(NoSuchElementException.class,
                () -> productoService.eliminarProducto(id));

        verify(validaciones, times(1)).validarIDProducto(id);
        verify(productoRepository, times(1)).existsById(id);
        verify(productoRepository, never()).deleteById(anyInt());
    }

    // ---- Productos ejemplo ----

    private Producto productoEjemplo1(int id){
        Producto producto = new Producto();
        producto.setId(id);
        producto.setNombre("Brownie");
        producto.setDescripcion("Chocolate");
        producto.setPrecio(BigDecimal.valueOf(39.50));
        producto.setDisponible(true);
        producto.setUnidades(50);
        producto.setIdCategoria(1);
        producto.setFechaRegistro(LocalDateTime.now().minusDays(1));
        producto.setFechaModificacion(LocalDateTime.now().minusHours(1));
        return producto;
    }

    private Producto productoEjemplo2(int id){
        Producto producto = new Producto();
        producto.setId(id);
        producto.setNombre("Pay de limon");
        producto.setDescripcion("Limon");
        producto.setPrecio(BigDecimal.valueOf(88.50));
        producto.setDisponible(true);
        producto.setUnidades(35);
        producto.setIdCategoria(6);
        producto.setFechaRegistro(LocalDateTime.now().minusDays(1));
        producto.setFechaModificacion(LocalDateTime.now().minusHours(1));
        return producto;
    }

}
