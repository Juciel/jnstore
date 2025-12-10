package br.com.jnstore.sboot.orch.client.venda;

import br.com.jnstore.sboot.atom.vendas.model.CaixaInput;
import br.com.jnstore.sboot.atom.vendas.model.CaixaRepresentation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "caixas", url = "${feign.client.venda.url}")
public interface CaixaClient {

    @GetMapping("/caixas")
    List<CaixaRepresentation> listarCaixas();

    @GetMapping("/caixas/{id}")
    CaixaRepresentation buscarCaixaPorId(@PathVariable("id") Long id);

    @GetMapping("/caixas/abrir")
    CaixaRepresentation consultaCaixaAbertoHoje();

    @PostMapping("/caixas/abrir")
    CaixaRepresentation abrirCaixa(@RequestBody CaixaInput input);

    @PostMapping("/caixas/{id}/fechar")
    CaixaRepresentation fecharCaixa(@PathVariable("id") Long id, @RequestBody(required = false) CaixaInput input);
}
