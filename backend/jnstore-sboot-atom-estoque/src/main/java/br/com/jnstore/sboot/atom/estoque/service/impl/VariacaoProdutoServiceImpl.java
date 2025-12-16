package br.com.jnstore.sboot.atom.estoque.service.impl;

import br.com.jnstore.sboot.atom.estoque.domain.TbVariacaoProduto;
import br.com.jnstore.sboot.atom.estoque.repository.VariacaoProdutoRepository;
import br.com.jnstore.sboot.atom.estoque.service.VariacaoProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VariacaoProdutoServiceImpl implements VariacaoProdutoService {

    private final VariacaoProdutoRepository repository;

    @Override
    public List<TbVariacaoProduto> getAll() {
        return repository.findAll();
    }

    @Override
    public Optional<TbVariacaoProduto> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public TbVariacaoProduto create(TbVariacaoProduto variacaoProduto) {
        return repository.save(variacaoProduto);
    }

    @Override
    @Transactional
    public TbVariacaoProduto update(TbVariacaoProduto variacaoProduto) {
        return repository.save(variacaoProduto);
    }

    @Override
    @Transactional
    public void delete(TbVariacaoProduto variacaoProduto) {
        repository.delete(variacaoProduto);
    }
}
