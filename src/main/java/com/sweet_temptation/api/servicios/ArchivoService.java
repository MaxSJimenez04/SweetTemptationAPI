package com.sweet_temptation.api.servicios;

import com.sweet_temptation.api.dto.ArchivoDTO;
import com.sweet_temptation.api.dto.DetallesArchivoDTO;
import com.sweet_temptation.api.model.Archivo;
import com.sweet_temptation.api.model.ImagenProducto;
import com.sweet_temptation.api.model.Producto;
import com.sweet_temptation.api.repository.ArchivoRepository;
import com.sweet_temptation.api.repository.ImagenClienteRepository;
import com.sweet_temptation.api.repository.ImagenProductoRepository;
import com.sweet_temptation.api.repository.ProductoRepository;
import com.sweet_temptation.api.validaciones.ArchivoValidator;
import com.sweet_temptation.api.validaciones.ImagenProductoValidator;
import com.sweet_temptation.api.validaciones.ProductoValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Importación necesaria

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
public class ArchivoService {

    private final ArchivoRepository repository;
    private final ImagenProductoRepository imagenProductoRepository;
    private final ImagenClienteRepository  imagenClienteRepository;
    private final ProductoRepository productoRepository;
    private final ArchivoValidator validaciones;
    private final ProductoValidator  productoValidator;
    private final ImagenProductoValidator asociacionValidator;

    public ArchivoService(ArchivoRepository repository, ImagenProductoRepository imagenProductoRepository ,
                          ImagenClienteRepository imagenClienteRepository,  ArchivoValidator validaciones,
                          ProductoValidator  productoValidator,  ProductoRepository productoRepository,
                          ImagenProductoValidator asociacionValidator) {
        this.repository = repository;
        this.imagenProductoRepository = imagenProductoRepository;
        this.imagenClienteRepository = imagenClienteRepository;
        this.validaciones = validaciones;
        this.productoValidator = productoValidator;
        this.productoRepository = productoRepository;
        this.asociacionValidator = asociacionValidator;
    }

    public int guardarArchivo(ArchivoDTO archivoDTO) {
        validaciones.validarArchivoPeticion(archivoDTO);
        validaciones.validarExtension(archivoDTO.getExtension());
        Archivo archivoNuevo = new Archivo();
        archivoNuevo.setDatos(archivoDTO.getDatos());
        archivoNuevo.setExtension(archivoDTO.getExtension());
        archivoNuevo.setFechaRegistro(LocalDateTime.now());
        Archivo archivoBD = repository.save(archivoNuevo);
        validaciones.validarArchivo(archivoBD);
        return archivoBD.getId();
    }

    /**
     * ASOCIACIÓN CORREGIDA (Lógica de Borrar y Reemplazar):
     * Borra la asociación existente y crea una nueva, resolviendo el error de restricción UNIQUE.
     */
    @Transactional // <--- ¡Asegura que el DELETE y el INSERT son atómicos!
    public void asociarArchivo(int idArchivo, int idProducto){
        validaciones.validarIDArchivo(idArchivo);
        productoValidator.validarIDProducto(idProducto);

        // 1. Obtener entidades
        Archivo archivoBD = repository.getReferenceById(idArchivo);
        Producto productoBD  = productoRepository.getReferenceById(idProducto);
        validaciones.validarArchivo(archivoBD);
        productoValidator.validarProducto(productoBD);

        // 2. Buscar si ya existe la asociación
        ImagenProducto asociacionExistente = imagenProductoRepository.findByIdProducto(idProducto);

        if (asociacionExistente != null) {
            // 🛑 Lógica de corrección: Borrar la asociación anterior para evitar
            // la violación de restricción UNIQUE en la base de datos.
            imagenProductoRepository.delete(asociacionExistente);
        }

        // 3. Crear el nuevo registro
        ImagenProducto nuevaAsociacion = new ImagenProducto();
        nuevaAsociacion.setIdProducto(idProducto);
        nuevaAsociacion.setIdArchivo(idArchivo);
        nuevaAsociacion.setFechaRegistro(archivoBD.getFechaRegistro());
        nuevaAsociacion.setFechaAsociacion(LocalDateTime.now());

        // 4. Guardar la nueva asociación (siempre será un INSERT en esta lógica)
        imagenProductoRepository.save(nuevaAsociacion);
    }

    public DetallesArchivoDTO obtenerDatosArchivo(int idProducto){
        productoValidator.validarIDProducto(idProducto);
        ImagenProducto asociacionBD = imagenProductoRepository.findByIdProducto(idProducto);

        // Si la asociación es null, lanza una excepción (se asume que debe existir)
        if (asociacionBD == null) {
            throw new NoSuchElementException("No existe asociación de archivo para el producto con ID: " + idProducto);
        }

        int idArchivo = asociacionBD.getIdArchivo();
        Archivo archivoBD = repository.getReferenceById(idArchivo);

        validaciones.validarArchivo(archivoBD);

        // Ruta RELATIVA
        String ruta = "/archivo/" + archivoBD.getId();
        System.out.println("RUTA ENVIADA: " + ruta);

        return new DetallesArchivoDTO(
                archivoBD.getId(),
                archivoBD.getFechaRegistro(),
                archivoBD.getExtension(),
                ruta
        );
    }

    public ArchivoDTO obtenerArchivo(int idArchivo){
        validaciones.validarIDArchivo(idArchivo);
        Archivo archivoBD = repository.getReferenceById(idArchivo);
        validaciones.validarArchivo(archivoBD);
        return new ArchivoDTO(archivoBD.getId(), archivoBD.getFechaRegistro(), archivoBD.getExtension(), archivoBD.getDatos());
    }
}
