package com.sweet_temptation.api.controller;

import com.sweet_temptation.api.dto.ProductoDTO;
import com.sweet_temptation.api.model.Producto;
import com.sweet_temptation.api.servicios.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequestMapping(path = "/producto")
public class ProductoController {
    @Autowired
    private ProductoService productoService;

    @GetMapping("/todos")
    public ResponseEntity<?> getProductos(){
        try{
            List<ProductoDTO> lista = productoService.consultarProductos();
            return  ResponseEntity.status(HttpStatus.OK).body(lista);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProducto(@PathVariable int id){
        try{
            ProductoDTO producto = productoService.consultarProducto(id);
            return ResponseEntity.status(HttpStatus.OK).body(producto);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch (NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping(path = "/nuevo")
    public ResponseEntity<?> crearProducto(@RequestBody ProductoDTO nuevoProducto){
        try{
            int idProductoNuevo = productoService.crearProducto(nuevoProducto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Producto creado con el id:" + idProductoNuevo);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<?> actualizarProducto(@PathVariable int id, @RequestBody ProductoDTO producto){
        try{
            ProductoDTO actualizado = productoService.actualizarProducto(id, producto);
            return ResponseEntity.status(HttpStatus.OK).body(actualizado);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch (NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> eliminarProducto(@PathVariable int id){
        try{
            productoService.eliminarProducto(id);
            return ResponseEntity.status(HttpStatus.OK).body("El producto fue eliminado");
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch (NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
