package br.com.jnstore.sboot.atom.estoque.service;

import br.com.jnstore.sboot.atom.estoque.domain.TbVariacaoProduto;

import java.util.List;
import java.util.Optional;

public interface VariacaoProdutoService {
    List<TbVariacaoProduto> getAll();

    Optional<TbVariacaoProduto> getById(Long id);

    TbVariacaoProduto create(TbVariacaoProduto produto);

    TbVariacaoProduto update(TbVariacaoProduto produto);

    void delete(TbVariacaoProduto produto);
}
