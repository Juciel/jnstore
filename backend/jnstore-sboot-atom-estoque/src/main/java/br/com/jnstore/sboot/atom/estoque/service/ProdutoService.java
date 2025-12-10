package br.com.jnstore.sboot.atom.estoque.service;

import br.com.jnstore.sboot.atom.estoque.domain.TbProduto;
import br.com.jnstore.sboot.atom.estoque.model.ProdutoRepresetation;

import java.util.List;
import java.util.Optional;

public interface ProdutoService {

    List<TbProduto> getAll();

    Optional<TbProduto> getById(Long id);

    TbProduto create(TbProduto produto);

    TbProduto update(TbProduto produto);

    void delete(TbProduto produto);

    String getNewSku();

    List<TbProduto> autocomplete(String termo);
}
