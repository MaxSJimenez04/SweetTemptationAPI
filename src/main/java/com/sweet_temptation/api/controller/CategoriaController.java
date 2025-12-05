package com.sweet_temptation.api.controller;

import com.sweet_temptation.api.dto.CategoriaDTO;
import com.sweet_temptation.api.servicios.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.NoSuchElementException;

@RestController // Usamos RestController ya que es una API REST
@RequestMapping(path = "/categoria")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping("/todos")
    public ResponseEntity<?> getCategorias(){
        try{
            List<CategoriaDTO> lista = categoriaService.consultarCategorias();
            return ResponseEntity.status(HttpStatus.OK).body(lista);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno al consultar categorías.");
        }
    }
}
