package com.sweet_temptation.api;

import com.sweet_temptation.api.dto.EstadisticaProductoDTO;
import com.sweet_temptation.api.dto.EstadisticaVentaProductoDTO;
import com.sweet_temptation.api.model.Producto;
import com.sweet_temptation.api.repository.EstadisticasProductoRepository;
import com.sweet_temptation.api.servicios.EstadisticasService;
import com.sweet_temptation.api.validaciones.EstadisticasValidator;
import com.sweet_temptation.api.validaciones.ProductoValidator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class EstadisticasProductosTest {
    @Mock
    private EstadisticasValidator estadisticasValidator;
    @Mock
    private ProductoValidator  productoValidator;
    @Mock
    private EstadisticasProductoRepository  estadisticasProductoRepository;

    @InjectMocks
    private EstadisticasService estadisticasService;

    private LocalDate fechaInicio = LocalDate.of(2025,12,1);
    private LocalDate fechaFin = LocalDate.of(2025,12,7);
    private int idProducto = 1;
    //Datos para mejores productos
    private final EstadisticaProductoDTO mejorProducto1 = new EstadisticaProductoDTO("MAS VENDIDOS",
            "Pastel de Fresa 1kg", 14);
    private final EstadisticaProductoDTO mejorProducto2 = new EstadisticaProductoDTO("MAS VENDIDOS",
            "Dona de Chocolate", 12);
    private final EstadisticaProductoDTO mejorProducto3 = new EstadisticaProductoDTO("MAS VENDIDOS",
            "Tarta de manzana", 9);
    private final EstadisticaProductoDTO mejorProducto4 = new EstadisticaProductoDTO("MAS VENDIDOS",
            "Pastel de Chocolate 1kg", 5);
    private final EstadisticaProductoDTO mejorProducto5 = new EstadisticaProductoDTO("MAS VENDIDOS",
            "Cupcake de Oreo", 3);

    //Datos para peores productos
    private final EstadisticaProductoDTO peorProducto1 = new EstadisticaProductoDTO("MENOS VENDIDOS",
            "Dona de Cajeta", 2);
    private final EstadisticaProductoDTO peorProducto2 = new EstadisticaProductoDTO("MENOS VENDIDOS",
            "Dona de Cajeta", 1);
    private final EstadisticaProductoDTO peorProducto3 = new EstadisticaProductoDTO("MENOS VENDIDOS",
            "Dona de Cajeta", 1);
    private final EstadisticaProductoDTO peorProducto4 = new EstadisticaProductoDTO("MENOS VENDIDOS",
            "Dona de Cajeta", 0);
    private final EstadisticaProductoDTO peorProducto5 = new EstadisticaProductoDTO("MENOS VENDIDOS",
            "Dona de Cajeta", 0);

    //Datos para consultar ventas de producto
    private final EstadisticaVentaProductoDTO ventaDia1= new EstadisticaVentaProductoDTO(Date.from(Instant.now())
            , 5);
    private final EstadisticaVentaProductoDTO ventaDia2= new EstadisticaVentaProductoDTO(Date.from(Instant.now())
            , 12);
    private final EstadisticaVentaProductoDTO ventaDia3= new EstadisticaVentaProductoDTO(Date.from(Instant.now())
            , 12);
    private final EstadisticaVentaProductoDTO ventaDia4= new EstadisticaVentaProductoDTO(Date.from(Instant.now())
            , 12);
    private final List<EstadisticaProductoDTO> estadisticasMejores = new ArrayList<>();
    private final List<EstadisticaProductoDTO> estadisticasPeores = new ArrayList<>();
    private final List<EstadisticaVentaProductoDTO> estadisticasProducto = new ArrayList<>();
    @BeforeEach
    public  void setUp()
    {
        estadisticasMejores.add(mejorProducto1);
        estadisticasMejores.add(mejorProducto2);
        estadisticasMejores.add(mejorProducto3);
        estadisticasMejores.add(mejorProducto4);
        estadisticasMejores.add(mejorProducto5);
        estadisticasPeores.add(peorProducto1);
        estadisticasPeores.add(peorProducto2);
        estadisticasPeores.add(peorProducto3);
        estadisticasPeores.add(peorProducto4);
        estadisticasPeores.add(peorProducto5);
        estadisticasProducto.add(ventaDia1);
        estadisticasProducto.add(ventaDia2);
        estadisticasProducto.add(ventaDia3);
        estadisticasProducto.add(ventaDia4);
    }


    @Test
    public void consultarProductosPopulares_Exito() {
        // Arrange
        when(estadisticasProductoRepository.obtenerMejoresVentas(fechaInicio, fechaFin)).thenReturn(estadisticasMejores);
        // Act
        List<EstadisticaProductoDTO> respuesta =
                estadisticasService.consultarProductosPopulares(fechaInicio, fechaFin);

        // Assert
        assertEquals(estadisticasMejores.size(), respuesta.size());
        verify(estadisticasValidator).validarLocalDate(fechaInicio, fechaFin);
        verify(estadisticasProductoRepository, times(1)).obtenerMejoresVentas(fechaInicio, fechaFin);

    }

    @Test
    public void consultarProductosPopulares_FechasInvalidas(){
        //Arrange
        fechaInicio = null;
        fechaFin = null;

        //Act
        doThrow(IllegalArgumentException.class).when(estadisticasValidator).validarLocalDate(fechaInicio, fechaFin);

        //Assert
        assertThrows(IllegalArgumentException.class, ()-> estadisticasService.consultarProductosPopulares(fechaInicio, fechaFin));
        verify(estadisticasValidator, times(1)).validarLocalDate(fechaInicio, fechaFin);
    }

    @Test
    public void consultarProductosImpopulares_Exito() {
        // Arrange
        LocalDate fechaInicio = LocalDate.of(2025, 10, 1);
        LocalDate fechaFin    = LocalDate.of(2025, 10, 31);
        when(estadisticasProductoRepository.obtenerPeoresVentas(fechaInicio, fechaFin)).thenReturn(estadisticasPeores);

        // Act
        List<EstadisticaProductoDTO> respuesta =
                estadisticasService.consultarProductosImpopulares(fechaInicio, fechaFin);

        // Assert
        assertNotNull(respuesta);
        assertEquals(estadisticasPeores.size(), respuesta.size());
        verify(estadisticasValidator).validarLocalDate(fechaInicio, fechaFin);
        verify(estadisticasProductoRepository, times(1)).obtenerPeoresVentas(fechaInicio, fechaFin);
    }


    @Test
    public void consultarProductosImpopulares_FechasInvalidas(){
        //Arrange
        fechaInicio = null;
        fechaFin = null;

        //Act
        doThrow(IllegalArgumentException.class).when(estadisticasValidator).validarLocalDate(fechaInicio, fechaFin);

        //Assert
        assertThrows(IllegalArgumentException.class, ()-> estadisticasService.consultarProductosPopulares(fechaInicio, fechaFin));
        verify(estadisticasValidator, times(1)).validarLocalDate(fechaInicio, fechaFin);
    }

    @Test
    public void obtenerEstadisticasProductos_Exito(){
        //Arrange
        when(estadisticasService.consultarProductosPopulares(fechaInicio, fechaFin)).thenReturn(estadisticasMejores);
        when(estadisticasService.consultarProductosImpopulares(fechaInicio, fechaFin)).thenReturn(estadisticasPeores);
        //Act
        List<EstadisticaProductoDTO> respuesta = estadisticasService.obtenerEstasticasProductos(fechaInicio, fechaFin);
        //Assert
        assertEquals(10, respuesta.size());
        verify(estadisticasValidator, times(5)).validarLocalDate(fechaInicio, fechaFin);
    }

    @Test
    public void obtenerEstadisticasProductos_FechasInvalidas(){
        //Arrange
        fechaInicio = null;
        fechaFin = null;
        //Act
        doThrow(IllegalArgumentException.class).when(estadisticasValidator).validarLocalDate(fechaInicio, fechaFin);
        //Assert
        assertThrows(IllegalArgumentException.class, () -> estadisticasService.obtenerEstasticasProductos(fechaInicio, fechaFin));
        verify(estadisticasValidator, times(1)).validarLocalDate(fechaInicio, fechaFin);
    }

    @Test
    public void obtenerVentasPorDia_Exito() {
        // Arrange
        LocalDate fechaInicio = LocalDate.of(2025, 10, 1);
        LocalDate fechaFin = LocalDate.of(2025, 10, 31);
        int idProducto = 1;
        when(estadisticasProductoRepository.obtenerVentasPorDia(fechaInicio, fechaFin, idProducto))
                .thenReturn(estadisticasProducto);
        // Act
        List<EstadisticaVentaProductoDTO> respuesta =
                estadisticasService.obtenerVentasPorDia(fechaInicio, fechaFin, idProducto);

        // Assert
        assertEquals(respuesta.size(), estadisticasProducto.size());
        verify(estadisticasValidator).validarLocalDate(fechaInicio, fechaFin);
        verify(productoValidator).validarIDProducto(idProducto);
        verify(estadisticasProductoRepository, times(1))
                .obtenerVentasPorDia(fechaInicio, fechaFin, idProducto);

    }

    @Test
    public void obtenerVentasPorDia_FechasInvalidas(){
        //Arrange
        fechaInicio = null;
        fechaFin = null;

        //Act
        doThrow(IllegalArgumentException.class).when(estadisticasValidator).validarLocalDate(fechaInicio, fechaFin);

        //Assert
        assertThrows(IllegalArgumentException.class,  () ->
                estadisticasService.obtenerVentasPorDia(fechaInicio, fechaFin, idProducto));
        verify(estadisticasValidator, times(1)).validarLocalDate(fechaInicio, fechaFin);

    }

    @Test
    public void obtenerVentasPorDia_IDInvalido(){
        //Arrange
        idProducto = 0;

        //Act
        doThrow(IllegalArgumentException.class).when(productoValidator).validarIDProducto(idProducto);

        //Assert
        assertThrows(IllegalArgumentException.class,  () ->
                estadisticasService.obtenerVentasPorDia(fechaInicio, fechaFin, idProducto));
        verify(estadisticasValidator, times(1)).validarLocalDate(fechaInicio, fechaFin);
        verify(productoValidator, times(1)).validarIDProducto(idProducto);
    }
}
