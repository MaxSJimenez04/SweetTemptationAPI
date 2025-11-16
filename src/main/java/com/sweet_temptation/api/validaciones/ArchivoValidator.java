package com.sweet_temptation.api.validaciones;

import com.sweet_temptation.api.dto.ArchivoDTO;
import com.sweet_temptation.api.model.Archivo;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Component
public class ArchivoValidator {
    public void validarArchivo(Archivo archivo){
        if(archivo == null){
            throw new NoSuchElementException("No existe el archivo");
        }
    }

    public void validarArchivoPeticion(ArchivoDTO archivoDTO){

        if (archivoDTO == null) {
            throw new IllegalArgumentException("Archivo no puede ser vacío");
        }
    }

    public void validarIDArchivo(int idArchivo){
        if (idArchivo <= 0) {
            throw new IllegalArgumentException("El ID del archivo es inválido");
        }
    }

    public void validarExtension(String extension){
        if (!(extension.equals("jpg") || extension.equals("png") || extension.equals("jpeg"))) {
            throw new IllegalArgumentException("Formato de extension del archivo no es valido");
        }
    }

}
