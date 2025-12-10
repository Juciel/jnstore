package br.com.jnstore.sboot.orch.controller;

import br.com.jnstore.sboot.atom.vendas.api.CaixasApi;
import br.com.jnstore.sboot.atom.vendas.model.CaixaInput;
import br.com.jnstore.sboot.atom.vendas.model.CaixaRepresentation;
import br.com.jnstore.sboot.orch.service.CaixaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CaixaController implements CaixasApi {

    private final CaixaService service;

    @Override
    public ResponseEntity<CaixaRepresentation> consultaCaixaAbertoHoje() {
        return ResponseEntity.ok(service.consultaCaixaAbertoHoje());
    }

    @Override
    public ResponseEntity<CaixaRepresentation> abrirCaixa(CaixaInput caixaInput) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.abrirCaixa(caixaInput));
    }

    @Override
    public ResponseEntity<CaixaRepresentation> fecharCaixa(Long id, CaixaInput caixaInput) {
        return ResponseEntity.ok(service.fecharCaixa(id, caixaInput));
    }

    @Override
    public ResponseEntity<List<CaixaRepresentation>> listarCaixas() {
        return ResponseEntity.ok(service.listarCaixas());
    }

    @Override
    public ResponseEntity<CaixaRepresentation> buscarCaixaPorId(Long id) {
        return ResponseEntity.ok(service.buscarCaixaPorId(id));
    }
}
