package br.com.jnstore.sboot.atom.vendas.controller;

import br.com.jnstore.sboot.atom.vendas.api.CaixasApi;
import br.com.jnstore.sboot.atom.vendas.mapper.CaixaMapper;
import br.com.jnstore.sboot.atom.vendas.model.CaixaInput;
import br.com.jnstore.sboot.atom.vendas.model.CaixaRepresentation;
import br.com.jnstore.sboot.atom.vendas.service.CaixaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CaixaController implements CaixasApi {

    private final CaixaService service;
    private final CaixaMapper mapper;

    @Override
    public ResponseEntity<CaixaRepresentation> consultaCaixaAbertoHoje() {
        return service.consultaCaixaAbertoHoje()
                .map(mapper::toRepresentation)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<CaixaRepresentation> abrirCaixa(CaixaInput caixaInput) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toRepresentation(service.abrirCaixa(caixaInput.getValor())));
    }

    @Override
    public ResponseEntity<CaixaRepresentation> fecharCaixa(Long id, CaixaInput caixaInput) {
        return ResponseEntity.ok(mapper.toRepresentation(service.fecharCaixa(id, caixaInput.getValor())));
    }

    @Override
    public ResponseEntity<CaixaRepresentation> retiradaCaixa(Long id, CaixaInput caixaInput) {
        return ResponseEntity.ok(mapper.toRepresentation(service.retiradaCaixa(id, caixaInput.getValor())));
    }

    @Override
    public ResponseEntity<List<CaixaRepresentation>> listarCaixas() {
        return ResponseEntity.ok(mapper.toRepresetationList(service.listarCaixas()));
    }

    @Override
    public ResponseEntity<CaixaRepresentation> buscarCaixaPorId(Long id) {
        return service.buscarCaixaPorId(id)
                .map(mapper::toRepresentation)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<Object> listarCaixasPaginado(Integer page, Integer size, LocalDate dataInicial, LocalDate dataFinal) {
        PageRequest pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(service.listarPaginado(pageable, dataInicial, dataFinal));
    }
}
