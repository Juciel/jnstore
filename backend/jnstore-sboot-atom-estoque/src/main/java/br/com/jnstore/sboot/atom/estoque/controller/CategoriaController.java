package br.com.jnstore.sboot.atom.estoque.controller;

import br.com.jnstore.sboot.atom.estoque.api.CategoriaApi;
import br.com.jnstore.sboot.atom.estoque.model.CategoriaRepresetation;
import br.com.jnstore.sboot.atom.estoque.service.CategoriaService;
import br.com.jnstore.sboot.atom.estoque.util.PaginationUtil; // Importar PaginationUtil
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoriaController implements CategoriaApi {

    private final CategoriaService categoriaService;

    @Override
    public ResponseEntity<List<CategoriaRepresetation>> listarCategorias() {
        return ResponseEntity.ok(categoriaService.listarTodas());
    }

    @Override
    public ResponseEntity<Object> listarCategoriasPaginado(Integer page, Integer size, List<String> sort, String descricao) {
        // Usa a utilidade para construir o objeto Sort
        Sort sortObject = PaginationUtil.createSortFromStrings(sort);

        // Cria o Pageable com os parâmetros de paginação e ordenação
        Pageable pageable = PageRequest.of(page, size, sortObject);

        return ResponseEntity.ok(categoriaService.listarPaginado(pageable, descricao));
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
