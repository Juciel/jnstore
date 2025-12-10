package br.com.jnstore.sboot.atom.estoque.service.impl;

import br.com.jnstore.sboot.atom.estoque.domain.TbCategoria;
import br.com.jnstore.sboot.atom.estoque.mapper.CategoriaMapper;
import br.com.jnstore.sboot.atom.estoque.model.CategoriaRepresetation;
import br.com.jnstore.sboot.atom.estoque.repository.CategoriaRepository;
import br.com.jnstore.sboot.atom.estoque.repository.ProdutoRepository;
import br.com.jnstore.sboot.atom.estoque.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final ProdutoRepository produtoRepository;
    private final CategoriaMapper categoriaMapper;

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaRepresetation> listarTodas() {
        return categoriaRepository.findAll().stream()
                .map(categoriaMapper::toRepresentation)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CategoriaRepresetation buscarPorId(Long id) {
        return categoriaRepository.findById(id)
                .map(categoriaMapper::toRepresentation)
                .orElseThrow(() -> new NoSuchElementException("Categoria com ID " + id + " não encontrada."));
    }

    @Override
    @Transactional
    public CategoriaRepresetation criar(CategoriaRepresetation categoria) {
        categoriaRepository.findByDescricaoIgnoreCase(categoria.getDescricao())
                .ifPresent(c -> {
                    throw new IllegalArgumentException("Já existe uma categoria com a descrição '" + categoria.getDescricao() + "'.");
                });
        TbCategoria entity = categoriaMapper.toEntity(categoria);
        return categoriaMapper.toRepresentation(categoriaRepository.save(entity));
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        TbCategoria entity = categoriaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Categoria com ID " + id + " não encontrada."));
        produtoRepository.findByCategoriaId(id).ifPresent(produto -> {
            throw new IllegalArgumentException("Não é possível remover categoria associada a produto.");
        });
        categoriaRepository.delete(entity);
    }

    @Override
    @Transactional
    public CategoriaRepresetation atualizarCategoria(Long id, CategoriaRepresetation categoria) {
        TbCategoria entity = categoriaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Categoria com ID " + id + " não encontrada."));

        categoriaRepository.findByDescricaoIgnoreCase(categoria.getDescricao())
                .ifPresent(categoriaExistente -> {
                    if (!categoriaExistente.getId().equals(id)) {
                        throw new IllegalArgumentException("Já existe uma categoria com a descrição '" + categoria.getDescricao() + "'.");
                    }
                });

        entity.setDescricao(categoria.getDescricao());
        return categoriaMapper.toRepresentation(categoriaRepository.save(entity));
    }
}
