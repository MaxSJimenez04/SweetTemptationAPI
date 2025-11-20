package com.sweet_temptation.api;

import com.sweet_temptation.api.dto.ArchivoDTO;
import com.sweet_temptation.api.dto.DetallesArchivoDTO;
import com.sweet_temptation.api.model.Archivo;
import com.sweet_temptation.api.model.ImagenProducto;
import com.sweet_temptation.api.model.Producto;
import com.sweet_temptation.api.repository.ArchivoRepository;
import com.sweet_temptation.api.repository.ImagenProductoRepository;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.sweet_temptation.api.repository.ProductoRepository;
import com.sweet_temptation.api.servicios.ArchivoService;
import com.sweet_temptation.api.validaciones.ArchivoValidator;
import com.sweet_temptation.api.validaciones.ImagenProductoValidator;
import com.sweet_temptation.api.validaciones.ProductoValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@ExtendWith(MockitoExtension.class)
public class ArchivoServiceTest {

    @Mock
    ArchivoRepository archivoRepository;
    @Mock
    ProductoRepository productoRepository;
    @Mock
    ImagenProductoRepository  imagenProductoRepository;
    @Mock
    ArchivoValidator archivoValidator;
    @Mock
    ProductoValidator productoValidator;
    @Mock
    ImagenProductoValidator  imagenProductoValidator;

    private Producto producto;
    private String datos = "Q*8!2X}mm3%!7#?mB4x8ExMgwf#CKN)}LzK0X9(6&]c50*";
    byte[] bytes = datos.getBytes();
    private ArchivoDTO archivoDTO;
    private ImagenProducto asociacion;
    private Archivo archivo;

    @InjectMocks
    ArchivoService archivoService;

    @BeforeEach
    public void setUp() {
        archivoDTO = new ArchivoDTO(1, LocalDateTime.now(), "png", bytes);
        asociacion =  new ImagenProducto(1, 1, 1, LocalDateTime.now(), LocalDateTime.now());
        producto = new Producto(1, "Pay de limon", "Pay de limon con base de galleta", BigDecimal.valueOf(100),
                true, 50, LocalDateTime.parse("2025-11-01T15:45:00"), LocalDateTime.now(), 6);
        archivo = new Archivo(1, LocalDateTime.now(), "png", bytes);
    }

    @Test
    void guardarArchivo_Exito() {
        //Arrange
        when(archivoRepository.save(any(Archivo.class))).thenReturn(archivo);

        //Act
        int id = archivoService.guardarArchivo(archivoDTO);

        //Assert
        assertEquals(archivo.getId(), id);
        verify(archivoValidator, times(1)).validarArchivoPeticion(archivoDTO);
        verify(archivoRepository, times(1)).save(any(Archivo.class));
        verify(archivoValidator, times(1)).validarArchivo(archivo);
    }

    @Test
    void guardarArchivo_ArchivoInvalido() {
        //Arrange
        archivoDTO = null;

        //Act
        doThrow(IllegalArgumentException.class).when(archivoValidator).validarArchivoPeticion(archivoDTO);

        //Assert
        assertThrows(IllegalArgumentException.class, () -> archivoService.guardarArchivo(archivoDTO));
        verify(archivoValidator, times(1)).validarArchivoPeticion(archivoDTO);
    }

    @Test
    void guardarArchivo_ExtensionInvalido() {
        //Arrange
        archivoDTO.setExtension("webp");

        //Act
        doThrow(IllegalArgumentException.class).when(archivoValidator).validarExtension(archivoDTO.getExtension());

        //Assert
        assertThrows(IllegalArgumentException.class, () -> archivoService.guardarArchivo(archivoDTO));
        verify(archivoValidator, times(1)).validarArchivoPeticion(archivoDTO);
        verify(archivoValidator, times(1)).validarExtension(archivoDTO.getExtension());
    }

    @Test
    void asociarArchivo_Exito() {
        //Arrange
        when(archivoRepository.getReferenceById(archivo.getId())).thenReturn(archivo);
        when(productoRepository.getReferenceById(producto.getId())).thenReturn(producto);

        //Act
        archivoService.asociarArchivo(archivo.getId(), producto.getId());

        //Assert
        verify(productoValidator, times(1)).validarIDProducto(producto.getId());
        verify(archivoValidator, times(1)).validarIDArchivo(archivo.getId());
        verify(archivoValidator, times(1)).validarArchivo(archivo);
        verify(productoValidator, times(1)).validarProducto(producto);
        verify(archivoRepository, times(1)).getReferenceById(archivo.getId());
        verify(productoRepository, times(1)).getReferenceById(producto.getId());
        verify(imagenProductoRepository, times(1)).save(any(ImagenProducto.class));
    }

    @Test
    void asociarArchivo_IDProductoInvalido(){
        //Arrange
        producto.setId(0);

        //Act
        doThrow(IllegalArgumentException.class).when(productoValidator).validarIDProducto(producto.getId());

        //Assert
        assertThrows(IllegalArgumentException.class, () -> archivoService.asociarArchivo(archivo.getId(), producto.getId()));
        verify(productoValidator, times(1)).validarIDProducto(producto.getId());
    }

    @Test
    void asociarArchivo_IDArchivoInvalido(){
        //Arrange
        archivo.setId(0);

        //Act
        doThrow(IllegalArgumentException.class).when(archivoValidator).validarIDArchivo(archivo.getId());

        //Assert
        assertThrows(IllegalArgumentException.class, () -> archivoService.asociarArchivo(archivo.getId(), producto.getId()));
        verify(archivoValidator, times(1)).validarIDArchivo(archivo.getId());

    }

    @Test
    void asociarArchivo_ProductoInvalido(){
        //Arrange
        int id = 5;
        when(productoRepository.getReferenceById(id)).thenReturn(null);
        when(archivoRepository.getReferenceById(archivo.getId())).thenReturn(archivo);

        //Act
        doThrow(NoSuchElementException.class).when(productoValidator).validarProducto(null);

        //Assert
        assertThrows(NoSuchElementException.class, () -> archivoService.asociarArchivo(archivo.getId(), id));
        verify(productoValidator, times(1)).validarIDProducto(id);
        verify(archivoValidator, times(1)).validarIDArchivo(archivo.getId());
        verify(archivoRepository, times(1)).getReferenceById(archivo.getId());
        verify(archivoValidator, times(1)).validarArchivo(archivo);
        verify(productoValidator, times(1)).validarProducto(null);
    }

    @Test
    void asociarArchivo_ArchivoInvalido(){
        //Arrange
        int id = 5;
        when(archivoRepository.getReferenceById(id)).thenReturn(null);
        when (productoRepository.getReferenceById(producto.getId())).thenReturn(producto);

        //Act
        doThrow(NoSuchElementException.class).when(archivoValidator).validarArchivo(null);

        //Assert
        assertThrows(NoSuchElementException.class, () -> archivoService.asociarArchivo(id, producto.getId()));
        verify(archivoValidator, times(1)).validarIDArchivo(id);
        verify(productoValidator, times(1)).validarIDProducto(producto.getId());
        verify(archivoValidator, times(1)).validarArchivo(null);
    }

    @Test
    void obtenerDatosArchivo_Exito(){
        //Arrange
        when(imagenProductoRepository.findByIdProducto(producto.getId())).thenReturn(asociacion);
        when(archivoRepository.getReferenceById(asociacion.getIdArchivo())).thenReturn(archivo);

        //Act
        DetallesArchivoDTO respuesta = archivoService.obtenerDatosArchivo(producto.getId());

        //Assert
        assertEquals(archivo.getId(), respuesta.getId());
        assertEquals(archivo.getExtension(), respuesta.getExtension());
        assertEquals(archivo.getFechaRegistro(), respuesta.getFechaRegistro());
        verify(imagenProductoRepository, times(1)).findByIdProducto(producto.getId());
        verify(archivoRepository, times(1)).getReferenceById(asociacion.getIdArchivo());
        verify(productoValidator, times(1)).validarIDProducto(producto.getId());
        verify(archivoValidator, times(1)).validarArchivo(any(Archivo.class));

    }

    @Test
    void obtenerDatosArchivo_IDInvalido(){
        //Arrange
        doThrow(IllegalArgumentException.class).when(productoValidator).validarIDProducto(producto.getId());

        //Act
        assertThrows(IllegalArgumentException.class, () -> archivoService.obtenerDatosArchivo(producto.getId()));

        //Assert
        verify(productoValidator, times(1)).validarIDProducto(producto.getId());
    }

    @Test
    void obtenerDatosArchivo_ArchivoInvalido(){
        //Arrange
        asociacion.setIdArchivo(5);
        when(imagenProductoRepository.findByIdProducto(producto.getId())).thenReturn(asociacion);
        when(archivoRepository.getReferenceById(asociacion.getIdArchivo())).thenReturn(null);

        //Act
        doThrow(NoSuchElementException.class).when(archivoValidator).validarArchivo(null);

        //Assert
        assertThrows(NoSuchElementException.class, () -> archivoService.obtenerDatosArchivo(producto.getId()));
        verify(productoValidator, times(1)).validarIDProducto(producto.getId());
        verify(imagenProductoRepository, times(1)).findByIdProducto(producto.getId());
        verify(archivoRepository, times(1)).getReferenceById(asociacion.getIdArchivo());
        verify(archivoValidator, times(1)).validarArchivo(null);
    }

    @Test
    void obtenerArchivo_Exito(){
        //Arrange
        when(archivoRepository.getReferenceById(archivo.getId())).thenReturn(archivo);

        //Act
        ArchivoDTO respuesta = archivoService.obtenerArchivo(archivo.getId());

        //Assert
        assertEquals(archivo.getId(), respuesta.getId());
        assertEquals(archivo.getExtension(), respuesta.getExtension());
        assertEquals(archivo.getFechaRegistro(), respuesta.getFechaRegistro());
        assertEquals(archivo.getDatos(), respuesta.getDatos());
        verify(archivoRepository, times(1)).getReferenceById(archivo.getId());
        verify(archivoValidator, times(1)).validarIDArchivo(archivo.getId());
        verify(archivoValidator, times(1)).validarArchivo(archivo);
    }

    @Test
    void obtenerArchivo_IDInvalido(){
        //Arrange
        archivo.setId(0);

        //Act
        doThrow(IllegalArgumentException.class).when(archivoValidator).validarIDArchivo(archivo.getId());

        //Assert
        assertThrows(IllegalArgumentException.class, () -> archivoService.obtenerArchivo(archivo.getId()));
        verify(archivoValidator, times(1)).validarIDArchivo(archivo.getId());
    }
    @Test
    void obtenerArchivo_ArchivoInvalido(){
        //Arrange
        int id = 5;
        when(archivoRepository.getReferenceById(id)).thenReturn(null);

        //Act
        doThrow(NoSuchElementException.class).when(archivoValidator).validarArchivo(null);

        //Assert
        assertThrows(NoSuchElementException.class, () -> archivoService.obtenerArchivo(id));
        verify(archivoValidator, times(1)).validarIDArchivo(id);
        verify(archivoRepository, times(1)).getReferenceById(id);
        verify(archivoValidator, times(1)).validarArchivo(null);
    }
}
