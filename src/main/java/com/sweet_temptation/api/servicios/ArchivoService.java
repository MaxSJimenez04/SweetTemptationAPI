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
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public void asociarArchivo(int idArchivo, int idProducto){
        validaciones.validarIDArchivo(idArchivo);
        productoValidator.validarIDProducto(idProducto);

        Archivo archivoBD = repository.getReferenceById(idArchivo);
        Producto productoBD  = productoRepository.getReferenceById(idProducto);
        validaciones.validarArchivo(archivoBD);
        productoValidator.validarProducto(productoBD);

        ImagenProducto asociacion = imagenProductoRepository.findByIdProducto(idProducto);

        if (asociacion == null) {
            asociacion = new ImagenProducto();
            asociacion.setIdProducto(idProducto);
            asociacion.setFechaRegistro(archivoBD.getFechaRegistro());
        } else {
            // Para la restricción UNIQUE.
        }

        asociacion.setIdArchivo(idArchivo);
        asociacion.setFechaAsociacion(LocalDateTime.now());

        imagenProductoRepository.save(asociacion);
    }

    public DetallesArchivoDTO obtenerDatosArchivo(int idProducto){
        productoValidator.validarIDProducto(idProducto);
        ImagenProducto asociacionBD = imagenProductoRepository.findByIdProducto(idProducto);

        if (asociacionBD == null) {
            throw new NoSuchElementException("No existe asociación de archivo para el producto con ID: " + idProducto);
        }

        int idArchivo = asociacionBD.getIdArchivo();
        Archivo archivoBD = repository.getReferenceById(idArchivo);

        validaciones.validarArchivo(archivoBD);

        String ruta = "/archivo/" + archivoBD.getId();

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

    @Transactional
    public void eliminarAsociacionYArchivoPorProducto(int idProducto) {
        ImagenProducto asociacion = imagenProductoRepository.findByIdProducto(idProducto);

        if (asociacion != null) {
            int idArchivo = asociacion.getIdArchivo();

            imagenProductoRepository.delete(asociacion);

            repository.deleteById(idArchivo);
        }
    }

    // Para modificar la imagen del producto
    @Transactional
    public void reemplazarImagenProducto(int idProducto, byte[] datosImagen, String extension) {

        eliminarAsociacionYArchivoPorProducto(idProducto);

        Archivo archivoNuevo = new Archivo();
        archivoNuevo.setDatos(datosImagen);
        archivoNuevo.setExtension(extension.toLowerCase().substring(0, Math.min(extension.length(), 10)));
        archivoNuevo.setFechaRegistro(LocalDateTime.now());

        Archivo archivoGuardado = repository.save(archivoNuevo);
        validaciones.validarArchivo(archivoGuardado);

        int idNuevoArchivo = archivoGuardado.getId();

        Producto productoBD = productoRepository.getReferenceById(idProducto);

        ImagenProducto nuevaAsociacion = new ImagenProducto();

        nuevaAsociacion.setIdProducto(idProducto);
        nuevaAsociacion.setIdArchivo(idNuevoArchivo);
        nuevaAsociacion.setFechaRegistro(archivoGuardado.getFechaRegistro()); // Usar la fecha del Archivo
        nuevaAsociacion.setFechaAsociacion(LocalDateTime.now());

        imagenProductoRepository.save(nuevaAsociacion);
        asociacionValidator.validarAsociacion(nuevaAsociacion); // Validar la asociación
    }
}