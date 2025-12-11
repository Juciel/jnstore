package br.com.jnstore.sboot.orch.client.produto;

import br.com.jnstore.sboot.atom.estoque.model.CategoriaRepresetation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "categorias", url = "${feign.client.produto.url}")
public interface CategoriaClient {

    @GetMapping("/categorias")
    List<CategoriaRepresetation> listarCategorias();

    @GetMapping("/categorias/{id}")
    CategoriaRepresetation buscarCategoriaPorId(@PathVariable("id") Long id);

    @PostMapping("/categorias")
    CategoriaRepresetation criarCategoria(@RequestBody CategoriaRepresetation categoriaRepresetation);

    @PutMapping("/categorias/{id}")
    CategoriaRepresetation atualizarCategoria(@PathVariable("id") Long id, @RequestBody CategoriaRepresetation categoriaRepresetation);

    @DeleteMapping("/categorias/{id}")
    void delete(@PathVariable("id") Long id);

    @GetMapping("/categorias/paginado")
    Object listarCategoriasPaginado(@RequestParam("page") Integer page,
                                    @RequestParam("size") Integer size,
                                    @RequestParam("sort") List<String> sort,
                                    @RequestParam("descricao") String descricao);
}
