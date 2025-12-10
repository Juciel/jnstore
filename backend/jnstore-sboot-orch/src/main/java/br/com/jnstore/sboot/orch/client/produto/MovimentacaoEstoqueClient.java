package br.com.jnstore.sboot.orch.client.produto;

import br.com.jnstore.sboot.atom.estoque.model.MovimentacaoEstoqueInput;
import br.com.jnstore.sboot.atom.estoque.model.MovimentacaoEstoqueRepresetation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "movimentacaoEstoque", url = "${feign.client.produto.url}")
public interface MovimentacaoEstoqueClient {

    @PostMapping("/estoque/entrada")
    MovimentacaoEstoqueRepresetation registrarEntrada(@RequestBody MovimentacaoEstoqueInput input);

    @PostMapping("/estoque/saida")
    MovimentacaoEstoqueRepresetation registrarSaida(@RequestBody MovimentacaoEstoqueInput input);
}
