package br.com.jnstore.sboot.atom.estoque.service.impl;

import br.com.jnstore.sboot.atom.estoque.domain.TbCategoria;
import br.com.jnstore.sboot.atom.estoque.mapper.CategoriaMapper;
import br.com.jnstore.sboot.atom.estoque.model.CategoriaRepresetation;
import br.com.jnstore.sboot.atom.estoque.repository.CategoriaRepository;
import br.com.jnstore.sboot.atom.estoque.repository.ProdutoRepository;
import br.com.jnstore.sboot.atom.estoque.service.CategoriaService;
import br.com.jnstore.sboot.atom.estoque.util.PaginationUtil; // Importar PaginationUtil
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
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
    public Page<CategoriaRepresetation> listarPaginado(Pageable pageable, String descricao) {
        // Aplica a ordenação padrão por ID descendente se a paginação não tiver ordenação
        Pageable sortedPageable = PaginationUtil.applyDefaultSortIfUnsorted(pageable, "id");

        Page<TbCategoria> pageResult;
        if (StringUtils.hasText(descricao)) {
            pageResult = categoriaRepository.findByDescricaoContainingIgnoreCase(descricao, sortedPageable);
        } else {
            pageResult = categoriaRepository.findAll(sortedPageable);
        }
        return pageResult.map(categoriaMapper::toRepresentation);
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
        entity.setIdUsuarioCriacao(1L); // Placeholder para usuário logado
        entity.setDataCriacao(LocalDateTime.now());
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
        entity.setIdUsuarioAtualizacao(1L); // Placeholder para usuário logado
        entity.setDataAtualizacao(LocalDateTime.now());
        return categoriaMapper.toRepresentation(categoriaRepository.save(entity));
    }
}
