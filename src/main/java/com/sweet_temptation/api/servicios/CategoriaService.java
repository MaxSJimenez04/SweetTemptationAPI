package com.sweet_temptation.api.servicios;


import com.sweet_temptation.api.dto.CategoriaDTO;
import com.sweet_temptation.api.model.Categoria;
import com.sweet_temptation.api.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Transactional(readOnly = true)
    public List<CategoriaDTO> consultarCategorias(){
        // Aquí podrías añadir validaciones si fuera necesario.
        List<Categoria> categorias = categoriaRepository.findAll();

        if (categorias.isEmpty()) {
            throw new NoSuchElementException("No se encontraron categorías registradas.");
        }

        // Mapea la lista de entidades a la lista de DTOs
        return categorias.stream().map(this::toDTO).toList();
    }

    // Método para convertir la Entidad Categoria a CategoriaDTO
    private CategoriaDTO toDTO(Categoria categoria){
        // *** IMPORTANTE: REEMPLAZA esto con la inicialización real de tu CategoriaDTO ***
        // Asumiendo que tu CategoriaDTO tiene constructor o setters para Id y Nombre.
        return new CategoriaDTO(categoria.getId(), categoria.getNombre());
    }
}
