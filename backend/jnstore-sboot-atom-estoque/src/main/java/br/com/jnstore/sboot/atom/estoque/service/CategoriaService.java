package br.com.jnstore.sboot.atom.estoque.service;

import br.com.jnstore.sboot.atom.estoque.model.CategoriaRepresetation;

import java.util.List;

public interface CategoriaService {

    List<CategoriaRepresetation> listarTodas();

    CategoriaRepresetation buscarPorId(Long id);

    CategoriaRepresetation criar(CategoriaRepresetation categoria);

    void deletar(Long id);

    CategoriaRepresetation atualizarCategoria(Long id, CategoriaRepresetation categoria);
}