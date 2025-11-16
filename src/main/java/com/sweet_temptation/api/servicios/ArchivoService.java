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

import java.time.LocalDateTime;

@Service
public class ArchivoService {

    private ArchivoRepository repository;
    private ImagenProductoRepository imagenProductoRepository;
    private ImagenClienteRepository  imagenClienteRepository;
    private ProductoRepository productoRepository;
    private ArchivoValidator validaciones;
    private ProductoValidator  productoValidator;
    private ImagenProductoValidator asociacionValidator;

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
        Archivo archivoNuevo = new Archivo();
        archivoNuevo.setDatos(archivoDTO.getDatos());
        archivoNuevo.setExtension(archivoDTO.getExtension());
        archivoNuevo.setFechaRegistro(LocalDateTime.now());
        Archivo archivoBD = repository.save(archivoNuevo);
        validaciones.validarArchivo(archivoBD);
        return archivoBD.getId();
    }

    public void asociarArchivo(int idArchivo, int idProducto){
        validaciones.validarIDArchivo(idArchivo);
        productoValidator.validarIDProducto(idProducto);
        Archivo archivoBD = repository.getReferenceById(idArchivo);
        Producto productoBD  = productoRepository.getReferenceById(idProducto);
        validaciones.validarArchivo(archivoBD);
        productoValidator.validarProducto(productoBD);
        ImagenProducto asociacion =  new ImagenProducto();
        asociacion.setIdProducto(idProducto);
        asociacion.setFechaRegistro(archivoBD.getFechaRegistro());
        asociacion.setIdArchivo(idArchivo);
        asociacion.setFechaAsociacion(LocalDateTime.now());
        imagenProductoRepository.save(asociacion);
    }

    public DetallesArchivoDTO obtenerDatosArchivo(int idProducto){
        productoValidator.validarIDProducto(idProducto);
        ImagenProducto asociacionBD =  imagenProductoRepository.findByIdProducto(idProducto);
        int idArchivo = asociacionBD.getIdArchivo();
        Archivo archivoBD = repository.getReferenceById(idArchivo);
        DetallesArchivoDTO detallesArchivo = new DetallesArchivoDTO(archivoBD.getId(),
                archivoBD.getFechaRegistro(), archivoBD.getExtension(), "http://localhost:8080/api/archivo/" + archivoBD.getId());
        return detallesArchivo;
    }

    public ArchivoDTO obtenerArchivo(int idArchivo){
        validaciones.validarIDArchivo(idArchivo);
        Archivo archivoBD = repository.getReferenceById(idArchivo);
        validaciones.validarArchivo(archivoBD);
        return new ArchivoDTO(archivoBD.getId(), archivoBD.getFechaRegistro(), archivoBD.getExtension(), archivoBD.getDatos());
    }


}
