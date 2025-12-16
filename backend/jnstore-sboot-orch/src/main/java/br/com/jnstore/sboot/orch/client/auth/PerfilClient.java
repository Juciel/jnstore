package br.com.jnstore.sboot.orch.client.auth;

import br.com.jnstore.sboot.auth.model.PerfilRepresentation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "perfilJnstore", url = "${feign.client.auth.url}")
public interface PerfilClient {

    @PostMapping("/api/profiles")
    PerfilRepresentation criarPerfil(@RequestBody() PerfilRepresentation perfilRepresentation);

    @GetMapping("/api/profiles/podeEditarProduto")
    Boolean podeEditarProduto();

    @GetMapping("/api/profiles/podeVisualizarProduto")
    Boolean podeVisualizarProduto();

    @GetMapping("/api/profiles/podeVisualizarValorCompra")
    Boolean podeVisualizarValorCompra();

    @GetMapping("/api/profiles/podeVisualizarCategoria")
    Boolean podeVisualizarCategoria();

    @GetMapping("/api/profiles/podeEditarCategoria")
    Boolean podeEditarCategoria();

    @GetMapping("/api/profiles/podeAbrirCaixa")
    Boolean podeAbrirCaixa();

    @GetMapping("/api/profiles/podeVisualizarCaixa")
    Boolean podeVisualizarCaixa();

    @GetMapping("/api/profiles/podeFecharCaixa")
    Boolean podeFecharCaixa();

    @GetMapping("/api/profiles/podeRetirarCaixa")
    Boolean podeRetirarCaixa();

    @GetMapping("/api/profiles/podeEditarVenda")
    Boolean podeEditarVenda();

    @GetMapping("/api/profiles/podeVisualizarVenda")
    Boolean podeVisualizarVenda();

    @GetMapping("/api/profiles/podeEditarValorVenda")
    Boolean podeEditarValorVenda();
}
