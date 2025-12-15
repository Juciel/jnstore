package br.com.jnstore.sboot.orch.client.produto;

import br.com.jnstore.sboot.atom.estoque.model.ProdutoRepresetation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "produto", url = "${feign.client.produto.url}")
public interface ProdutoClient {

    @GetMapping("/api/produtos")
    List<ProdutoRepresetation> getAll();

    @GetMapping("/api/produtos/{id}")
    ProdutoRepresetation getById(@PathVariable("id") Long id);

    @PostMapping("/api/produtos")
    ProdutoRepresetation create(@RequestBody ProdutoRepresetation produto);

    @PutMapping("/api/produtos/{id}")
    ProdutoRepresetation update(@PathVariable("id") Long id, @RequestBody ProdutoRepresetation produto);

    @DeleteMapping("/api/produtos/{id}")
    void delete(@PathVariable("id") Long id);

    @GetMapping("/api/produtos/new-sku")
    String getNewSku();

    @GetMapping("/api/produtos/por-variacao")
    List<ProdutoRepresetation> listarProdutosPorIdVariacao(@RequestParam("idVariacao") List<Long> idVariacao);

    @GetMapping("/api/produtos/paginado")
    Object getAllPaginado(@RequestParam("page") Integer page,
                          @RequestParam("size") Integer size,
                          @RequestParam("sort") List<String> sort,
                          @RequestParam("termo") String termo);

    @GetMapping("/api/produtos/baixo-estoque")
    List<ProdutoRepresetation> getBaixoEstoque(@RequestParam("limit") Integer limit);

    @GetMapping("/api/produtos/em-falta")
    List<ProdutoRepresetation> getEmFalta(@RequestParam("limit") Integer limit);
}
