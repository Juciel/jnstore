package br.com.jnstore.sboot.orch.controller;

import br.com.jnstore.sboot.atom.estoque.api.CategoriasApi;
import br.com.jnstore.sboot.atom.estoque.model.CategoriaRepresetation;
import br.com.jnstore.sboot.orch.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoriaController implements CategoriasApi {

    private final CategoriaService categoriaService;

    @Override
    public ResponseEntity<List<CategoriaRepresetation>> listarCategorias() {
        return ResponseEntity.ok(categoriaService.listarCategorias());
    }

    @Override
    public ResponseEntity<CategoriaRepresetation> buscarCategoriaPorId(Long id) {
        return ResponseEntity.ok(categoriaService.buscarCategoriaPorId(id));
    }

    @Override
    public ResponseEntity<CategoriaRepresetation> criarCategoria(CategoriaRepresetation categoriaRepresetation) {
        return new ResponseEntity<>(categoriaService.criarCategoria(categoriaRepresetation), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<CategoriaRepresetation> atualizarCategoria(Long id, CategoriaRepresetation categoriaRepresetation) {
        return ResponseEntity.ok(categoriaService.atualizarCategoria(id, categoriaRepresetation));
    }

    @Override
    public ResponseEntity<Void> deletarCategoria(Long id) {
        categoriaService.deletarCategoria(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Object> listarCategoriasPaginado(Integer page, Integer size,List<String> sort, String descricao) {
        return ResponseEntity.ok(categoriaService.listarCategoriasPaginado(page, size, sort, descricao));
    }
}