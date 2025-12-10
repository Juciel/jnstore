package br.com.jnstore.sboot.atom.estoque.controller;

import br.com.jnstore.sboot.atom.estoque.api.CategoriasApi;
import br.com.jnstore.sboot.atom.estoque.model.CategoriaRepresetation;
import br.com.jnstore.sboot.atom.estoque.service.CategoriaService;
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
        return ResponseEntity.ok(categoriaService.listarTodas());
    }

    @Override
    public ResponseEntity<CategoriaRepresetation> buscarCategoriaPorId(Long id) {
        return ResponseEntity.ok(categoriaService.buscarPorId(id));
    }

    @Override
    public ResponseEntity<CategoriaRepresetation> criarCategoria(CategoriaRepresetation categoria) {
        return new ResponseEntity<>(categoriaService.criar(categoria), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<CategoriaRepresetation> atualizarCategoria(Long id, CategoriaRepresetation categoriaRepresetation) {
        return ResponseEntity.ok(categoriaService.atualizarCategoria(id, categoriaRepresetation));
    }

    @Override
    public ResponseEntity<Void> deletarCategoria(Long id) {
        categoriaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}