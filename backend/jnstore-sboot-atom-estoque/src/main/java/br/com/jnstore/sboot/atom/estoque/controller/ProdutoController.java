package br.com.jnstore.sboot.atom.estoque.controller;

import br.com.jnstore.sboot.atom.estoque.api.ProdutosApi;
import br.com.jnstore.sboot.atom.estoque.mapper.ProdutoMapper;
import br.com.jnstore.sboot.atom.estoque.model.ProdutoRepresetation;
import br.com.jnstore.sboot.atom.estoque.service.ProdutoService;
import br.com.jnstore.sboot.atom.estoque.util.PaginationUtil; // Importar PaginationUtil
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort; // Importar Sort
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProdutoController implements ProdutosApi {

    private final ProdutoService service;
    private final ProdutoMapper mapper;

    @Override
    public ResponseEntity<List<ProdutoRepresetation>> getAll() {
        return ResponseEntity.ok(mapper.toRepresetationList(service.getAll()));
    }

    @Override
    public ResponseEntity<Object> getAllPaginado(Integer page, Integer size, List<String> sort, String termo) { // Adicionado List<String> sort
        // Usa a utilidade para construir o objeto Sort
        Sort sortObject = PaginationUtil.createSortFromStrings(sort);

        // Cria o Pageable com os parâmetros de paginação e ordenação
        Pageable pageable = PageRequest.of(page, size, sortObject);

        return ResponseEntity.ok(service.getAllPaginado(pageable, termo));
    }

    @Override
    public ResponseEntity<ProdutoRepresetation> create(@RequestBody ProdutoRepresetation produtoRepresetation) {
        return ResponseEntity.ok(mapper.toRepresetation(service.create(mapper.toDomain(produtoRepresetation))));
    }

    @Override
    public ResponseEntity<ProdutoRepresetation> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(mapper::toRepresetation)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<ProdutoRepresetation> update(@PathVariable Long id, @RequestBody ProdutoRepresetation produtoRepresetation) {
        return service.getById(id)
                .map(produtoExistente -> {
                    var produtoParaAtualizar = mapper.toDomain(produtoRepresetation);
                    produtoParaAtualizar.setId(id);
                    produtoParaAtualizar.setDataCriacao(produtoExistente.getDataCriacao());
                    produtoParaAtualizar.setIdUsuarioCriacao(produtoExistente.getIdUsuarioCriacao());
                    var produtoAtualizado = service.update(produtoParaAtualizar);
                    return ResponseEntity.ok(mapper.toRepresetation(produtoAtualizado));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return service.getById(id)
                .map(produto -> {
                    service.delete(produto);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<String> getNewSku() {
        return ResponseEntity.ok(service.getNewSku());
    }

    @Override
    public ResponseEntity<List<ProdutoRepresetation>> getProdutosPorVariacao(List<Long> idVariacao) {
        return ResponseEntity.ok(mapper.toRepresetationList(service.getProdutosPorVariacao(idVariacao)));
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
