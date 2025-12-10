package br.com.jnstore.sboot.orch.client.venda;

import br.com.jnstore.sboot.atom.vendas.model.VendaInput;
import br.com.jnstore.sboot.atom.vendas.model.VendaRepresentation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "vendas", url = "${feign.client.venda.url}")
public interface VendaClient {

    @GetMapping("/vendas")
    List<VendaRepresentation> listarVendas();

    @GetMapping("/vendas/{id}")
    VendaRepresentation buscarVendaPorId(@PathVariable("id") Long id);

    @PostMapping("/vendas")
    VendaRepresentation registrarVenda(@RequestBody VendaInput input);

    @DeleteMapping("/vendas/{id}")
    void desfazerVenda(@PathVariable("id") Long id);

    @GetMapping("/vendas/por-variacao")
    List<VendaRepresentation> listarVendasPorIdVariacao(@RequestParam("idVariacao") List<Long> idVariacao);
}
