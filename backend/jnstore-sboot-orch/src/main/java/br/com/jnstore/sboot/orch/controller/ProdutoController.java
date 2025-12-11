package br.com.jnstore.sboot.orch.controller;

import br.com.jnstore.sboot.atom.estoque.api.ProdutosApi;
import br.com.jnstore.sboot.atom.estoque.model.ProdutoRepresetation;
import br.com.jnstore.sboot.atom.vendas.model.ItemVendaProdutoRepresentation;
import br.com.jnstore.sboot.orch.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProdutoController implements ProdutosApi {

    private final ProdutoService service;

    @Override
    public ResponseEntity<List<ProdutoRepresetation>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @Override
    public ResponseEntity<ProdutoRepresetation> create(ProdutoRepresetation produtoRepresetation) {
        return ResponseEntity.ok(service.create(produtoRepresetation));
    }

    @Override
    public ResponseEntity<ProdutoRepresetation> getById(Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @Override
    public ResponseEntity<ProdutoRepresetation> update(Long id, ProdutoRepresetation produtoRepresetation) {
        return ResponseEntity.ok(service.update(id, produtoRepresetation));
    }

    @Override
    public ResponseEntity<Void> delete(Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<String> getNewSku() {
        return ResponseEntity.ok(service.getNewSku());
    }

    @Override
    public ResponseEntity<Object> getAllPaginado(Integer page, Integer size, List<String> sort, String termo) {
        return ResponseEntity.ok(service.getAllPaginado(page, size, sort, termo));
    }

    @Override
    public ResponseEntity<List<ProdutoRepresetation>> getBaixoEstoque(Integer limit) {
        return ResponseEntity.ok(service.getBaixoEstoque(limit));
    }

    @Override
    public ResponseEntity<List<ProdutoRepresetation>> getEmFalta(Integer limit) {
        return ResponseEntity.ok(service.getEmFalta(limit));
    }
}
