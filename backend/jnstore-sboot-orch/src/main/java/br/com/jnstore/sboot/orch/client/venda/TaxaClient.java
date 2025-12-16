package br.com.jnstore.sboot.orch.client.venda;

import br.com.jnstore.sboot.atom.vendas.model.TaxaInput;
import br.com.jnstore.sboot.atom.vendas.model.TaxaRepresentation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "taxas", url = "${feign.client.venda.url}")
public interface TaxaClient {

    @GetMapping("/api/taxas/{id}")
    TaxaRepresentation getTaxaPorId(@PathVariable("id") Long id);

    @GetMapping("/api/taxas")
    TaxaRepresentation getTaxaPorNome(@RequestParam("nome") String nome);

    @PostMapping("/api/taxas")
    TaxaRepresentation inserirTaxa(@RequestBody TaxaInput taxaInput);

    @PostMapping("/api/taxas/{id}")
    TaxaRepresentation atualizarTaxa(@PathVariable("id") Long id, @RequestBody TaxaInput taxaInput);
}
