package com.ank.pedidos.services;

import com.ank.pedidos.controllers.dto.Filtro.FiltroProdutoDto;
import com.ank.pedidos.controllers.dto.ImageUploadResponse;
import com.ank.pedidos.controllers.dto.ProdutoRequest;
import com.ank.pedidos.controllers.dto.ProdutoResponse;
import com.ank.pedidos.controllers.mapper.ProdutoMapper;
import com.ank.pedidos.controllers.spec.ProdutoSpec;
import com.ank.pedidos.entities.ImageData;
import com.ank.pedidos.entities.Produto;
import com.ank.pedidos.repositories.CategoriaRepository;
import com.ank.pedidos.repositories.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProdutoService {
    @Autowired
    ProdutoRepository produtoRepository;

    @Autowired
    CategoriaRepository categoriaRepository;

    @Autowired
    ImageDataService imageDataService;

    public Produto save(ProdutoRequest produtoRequest) {
        return produtoRepository.save(ProdutoMapper.INSTANCE.toEntity(produtoRequest));
    }

    public Page<ProdutoResponse> findAll(
            FiltroProdutoDto filtro,
            Pageable pageable
    ) {
        Page<Produto> entityPage = produtoRepository.findAll(ProdutoSpec.toSpec(filtro), pageable);
        List<ProdutoResponse> dtoList = entityPage
                .stream()
                .map(ProdutoMapper.INSTANCE::toResponse)
                .collect(Collectors.toList());

        return new PageImpl<>(dtoList, pageable, entityPage.getTotalElements());
    }

    public void delete(Long id) {
        produtoRepository.deleteById(id);
    }

    @Transactional
    public Produto update(ProdutoRequest produtoRequest, Long id) {
        Produto produto = produtoRepository.findById(id).orElseThrow();
        ProdutoMapper.INSTANCE.updateProdutoFromDto(produtoRequest, produto);
        produto.setCategoria(categoriaRepository.findById(produtoRequest.getCategoria()).orElseThrow());
        return produtoRepository.save(produto);
    }

    public ProdutoResponse findById(Long id) {
        return ProdutoMapper.INSTANCE.toResponse(produtoRepository.findById(id).orElseThrow());
    }

    public ImageUploadResponse setProdutoImage(MultipartFile imagem, Long idProduto) throws IOException {
        Produto produto = produtoRepository.findById(idProduto).orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        return imageDataService.uploadImage(imagem, produto);

    }

    public void deleteImgFromProduto(Long idProduto, Long idImage) {
        ImageData image = imageDataService.getImagesByProdutoId(idProduto).stream()
                .filter(img -> img.getId().equals(idImage))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Imagem não encontrada."));
        imageDataService.delete(image);
    }
}
