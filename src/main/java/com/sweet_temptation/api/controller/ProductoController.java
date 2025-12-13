package com.sweet_temptation.api.controller;

import com.sweet_temptation.api.dto.ProductoDTO;
import com.sweet_temptation.api.model.Producto;
import com.sweet_temptation.api.servicios.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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
            return ResponseEntity.ok(idProductoNuevo);

        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping(path = "/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> actualizarProducto(
            @PathVariable int id,
            @RequestPart("producto") ProductoDTO producto, // Recibe los datos JSON/form-data
            @RequestPart(value = "imagen", required = false) MultipartFile imagenNueva) { // Recibe el archivo de imagen (opcional)
        try {
            // Llamamos al servicio con los datos y el archivo
            ProductoDTO actualizado = productoService.actualizarProducto(id, producto, imagenNueva);
            return ResponseEntity.status(HttpStatus.OK).body(actualizado);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error de argumento: " + e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (RuntimeException e) {
            // Captura errores generales como problemas de lectura del archivo o de DB
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno al actualizar el producto o la imagen: " + e.getMessage());
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

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<?> getProductoPorNombre(@PathVariable String nombre) {
        try {
            ProductoDTO producto = productoService.consultarProductoPorNombre(nombre);
            return ResponseEntity.status(HttpStatus.OK).body(producto);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El producto con el nombre '" + nombre + "' no fue encontrado.");
        }
    }
}
