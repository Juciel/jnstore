package br.com.jnstore.sboot.orch.service;

import br.com.jnstore.sboot.auth.model.PerfilRepresentation;
import br.com.jnstore.sboot.orch.client.auth.PerfilClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PerfilService {

    private final PerfilClient client;

    public PerfilRepresentation criarPerfil(PerfilRepresentation perfilRepresentation) {
        return client.criarPerfil(perfilRepresentation);
    }

    public Boolean podeEditarProduto() {
        return client.podeEditarProduto();
    }

    public Boolean podeVisualizarProduto() {
        return client.podeVisualizarProduto();
    }

    public Boolean podeVisualizarValorCompra() {
        return client.podeVisualizarValorCompra();
    }

    public Boolean podeVisualizarCategoria() {
        return client.podeVisualizarCategoria();
    }

    public Boolean podeEditarCategoria() {
        return client.podeEditarCategoria();
    }

    public Boolean podeAbrirCaixa() {
        return client.podeAbrirCaixa();
    }

    public Boolean podeVisualizarCaixa() {
        return client.podeVisualizarCaixa();
    }

    public Boolean podeFecharCaixa() {
        return client.podeFecharCaixa();
    }

    public Boolean podeRetirarCaixa() {
        return client.podeRetirarCaixa();
    }

    public Boolean podeEditarVenda() {
        return client.podeEditarVenda();
    }

    public Boolean podeVisualizarVenda() {
        return client.podeVisualizarVenda();
    }

    public Boolean podeEditarValorVenda() {
        return client.podeEditarValorVenda();
    }
}
