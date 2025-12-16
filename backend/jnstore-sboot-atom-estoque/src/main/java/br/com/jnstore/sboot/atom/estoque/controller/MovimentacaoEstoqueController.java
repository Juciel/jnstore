package br.com.jnstore.sboot.atom.estoque.controller;

import br.com.jnstore.sboot.atom.estoque.api.EstoqueApi;
import br.com.jnstore.sboot.atom.estoque.model.MovimentacaoEstoqueInput;
import br.com.jnstore.sboot.atom.estoque.model.MovimentacaoEstoqueRepresetation;
import br.com.jnstore.sboot.atom.estoque.service.MovimentacaoEstoqueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MovimentacaoEstoqueController implements EstoqueApi {

    private final MovimentacaoEstoqueService service;

    @Override
    public ResponseEntity<MovimentacaoEstoqueRepresetation> registrarEntrada(MovimentacaoEstoqueInput movimentacaoEstoqueInput) {
        service.registrarEntrada(movimentacaoEstoqueInput);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<MovimentacaoEstoqueRepresetation> registrarSaida(MovimentacaoEstoqueInput movimentacaoEstoqueInput) {
        service.registrarSaida(movimentacaoEstoqueInput);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}