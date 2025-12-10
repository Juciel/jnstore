package br.com.jnstore.sboot.orch.service;

import br.com.jnstore.sboot.atom.estoque.model.CategoriaRepresetation;
import br.com.jnstore.sboot.orch.client.produto.CategoriaClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaClient categoriaClient;

    public List<CategoriaRepresetation> listarCategorias() {
        return categoriaClient.listarCategorias();
    }

    public CategoriaRepresetation criarCategoria(CategoriaRepresetation categoria) {
        return categoriaClient.criarCategoria(categoria);
    }

    public CategoriaRepresetation buscarCategoriaPorId(Long id) {
        return categoriaClient.buscarCategoriaPorId(id);
    }

    public CategoriaRepresetation atualizarCategoria(Long id, CategoriaRepresetation categoria) {
        return categoriaClient.atualizarCategoria(id, categoria);
    }

    public void deletarCategoria(Long id) {
        categoriaClient.delete(id);
    }
}