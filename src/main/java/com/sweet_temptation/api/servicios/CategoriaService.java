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
        List<Categoria> categorias = categoriaRepository.findAll();

        if (categorias.isEmpty()) {
            throw new NoSuchElementException("No se encontraron categorías registradas.");
        }
        return categorias.stream().map(this::toDTO).toList();
    }

    private CategoriaDTO toDTO(Categoria categoria){
        return new CategoriaDTO(categoria.getId(), categoria.getNombre());
    }
}
