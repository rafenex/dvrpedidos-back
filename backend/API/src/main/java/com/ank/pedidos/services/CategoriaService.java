package com.ank.pedidos.services;

import com.ank.pedidos.controllers.dto.Filtro.FiltroCategoriaDto;
import com.ank.pedidos.controllers.spec.CategoriaSpec;
import com.ank.pedidos.entities.Categoria;
import com.ank.pedidos.repositories.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


@Service
public class CategoriaService {
    @Autowired
    private CategoriaRepository categoriaRepository;

    public Categoria save(Categoria categoria){
        return categoriaRepository.save(categoria);
    }

    public Page<Categoria> findAll(FiltroCategoriaDto filtro,
                                   Pageable pageable){
        Specification<Categoria> spec = CategoriaSpec.toSpec(filtro);
        return categoriaRepository.findAll(spec, pageable);
    }

    public void delete(Long id){
        categoriaRepository.deleteById(id);
    }

    public Categoria update(Categoria categoria, Long id){
        Categoria categoriaToUpdate = categoriaRepository.findById(id).orElseThrow();
        categoriaToUpdate.setNome(categoria.getNome());
        return categoriaRepository.save(categoriaToUpdate);
    }

    public Categoria findById(Long id) {
        return categoriaRepository.findById(id).orElseThrow();
    }
}
