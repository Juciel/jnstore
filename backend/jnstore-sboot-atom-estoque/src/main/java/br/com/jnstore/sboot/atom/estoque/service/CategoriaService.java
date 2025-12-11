package br.com.jnstore.sboot.atom.estoque.service;

import br.com.jnstore.sboot.atom.estoque.model.CategoriaRepresetation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoriaService {

    List<CategoriaRepresetation> listarTodas();

    Page<CategoriaRepresetation> listarPaginado(Pageable pageable, String descricao);

    CategoriaRepresetation buscarPorId(Long id);

    CategoriaRepresetation criar(CategoriaRepresetation categoria);

    void deletar(Long id);

    CategoriaRepresetation atualizarCategoria(Long id, CategoriaRepresetation categoria);
}