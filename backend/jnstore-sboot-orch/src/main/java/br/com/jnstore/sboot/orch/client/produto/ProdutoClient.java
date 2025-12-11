package br.com.jnstore.sboot.orch.client.produto;

import br.com.jnstore.sboot.atom.estoque.model.ProdutoRepresetation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "produto", url = "${feign.client.produto.url}")
public interface ProdutoClient {

    @GetMapping("/produtos")
    List<ProdutoRepresetation> getAll();

    @GetMapping("/produtos/{id}")
    ProdutoRepresetation getById(@PathVariable("id") Long id);

    @PostMapping("/produtos")
    ProdutoRepresetation create(@RequestBody ProdutoRepresetation produto);

    @PutMapping("/produtos/{id}")
    ProdutoRepresetation update(@PathVariable("id") Long id, @RequestBody ProdutoRepresetation produto);

    @DeleteMapping("/produtos/{id}")
    void delete(@PathVariable("id") Long id);

    @GetMapping("/produtos/new-sku")
    String getNewSku();

    @GetMapping("/produtos/por-variacao")
    List<ProdutoRepresetation> listarProdutosPorIdVariacao(@RequestParam("idVariacao") List<Long> idVariacao);

    @GetMapping("/produtos/paginado")
    Object getAllPaginado(@RequestParam("page") Integer page,
                          @RequestParam("size") Integer size,
                          @RequestParam("sort") List<String> sort,
                          @RequestParam("termo") String termo);
}
