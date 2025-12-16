package br.com.jnstore.sboot.orch.controller;

import br.com.jnstore.sboot.auth.api.PerfilApi;
import br.com.jnstore.sboot.auth.model.PerfilRepresentation;
import br.com.jnstore.sboot.orch.service.PerfilService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PerfilController implements PerfilApi {

    private final PerfilService service;

    @Override
    public ResponseEntity<PerfilRepresentation> criarPerfil(PerfilRepresentation perfilRepresentation) {
        return new ResponseEntity<>(service.criarPerfil(perfilRepresentation), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Boolean> podeEditarProduto() {
        return ResponseEntity.ok(service.podeEditarProduto());
    }

    @Override
    public ResponseEntity<Boolean> podeVisualizarProduto() {
        return ResponseEntity.ok(service.podeVisualizarProduto());
    }

    @Override
    public ResponseEntity<Boolean> podeVisualizarValorCompra() {
        return ResponseEntity.ok(service.podeVisualizarValorCompra());
    }

    @Override
    public ResponseEntity<Boolean> podeVisualizarCategoria() {
        return ResponseEntity.ok(service.podeVisualizarCategoria());
    }

    @Override
    public ResponseEntity<Boolean> podeEditarCategoria() {
        return ResponseEntity.ok(service.podeEditarCategoria());
    }

    @Override
    public ResponseEntity<Boolean> podeAbrirCaixa() {
        return ResponseEntity.ok(service.podeAbrirCaixa());
    }

    @Override
    public ResponseEntity<Boolean> podeVisualizarCaixa() {
        return ResponseEntity.ok(service.podeVisualizarCaixa());
    }

    @Override
    public ResponseEntity<Boolean> podeFecharCaixa() {
        return ResponseEntity.ok(service.podeFecharCaixa());
    }

    @Override
    public ResponseEntity<Boolean> podeRetirarCaixa() {
        return ResponseEntity.ok(service.podeRetirarCaixa());
    }

    @Override
    public ResponseEntity<Boolean> podeEditarVenda() {
        return ResponseEntity.ok(service.podeEditarVenda());
    }

    @Override
    public ResponseEntity<Boolean> podeVisualizarVenda() {
        return ResponseEntity.ok(service.podeVisualizarVenda());
    }

    @Override
    public ResponseEntity<Boolean> podeEditarValorVenda() {
        return ResponseEntity.ok(service.podeEditarValorVenda());
    }
}
