package com.sweet_temptation.api.validaciones;

import com.sweet_temptation.api.model.ImagenProducto;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Component
public class ImagenProductoValidator {
    public void validarIDAsociacion(int idAsociacion){
        if(idAsociacion <= 0){
            throw new IllegalArgumentException("ID inválido");
        }
    }

    public void validarAsociacion(ImagenProducto imagenProducto){
        if(imagenProducto == null){
            throw new NoSuchElementException("No existe la asociación entre archivo y producto");
        }
        if(imagenProducto.getIdProducto() <= 0 || imagenProducto.getIdArchivo() <= 0){
            throw new IllegalArgumentException("ID producto o archivo inválidos");
        }
    }
}
