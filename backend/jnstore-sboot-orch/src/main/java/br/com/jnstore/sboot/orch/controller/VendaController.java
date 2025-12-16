package br.com.jnstore.sboot.orch.controller;

import br.com.jnstore.sboot.atom.vendas.api.VendaApi;
import br.com.jnstore.sboot.atom.vendas.model.*;
import br.com.jnstore.sboot.orch.service.VendaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class VendaController implements VendaApi {

    private final VendaService service;

    @Override
    public ResponseEntity<VendaRepresentation> registrarVenda(VendaInput vendaInput) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registrarVenda(vendaInput));
    }

    @Override
    public ResponseEntity<List<VendaRepresentation>> listarVendas() {
        return ResponseEntity.ok(service.listarVendas());
    }

    @Override
    public ResponseEntity<VendaRepresentation> buscarVendaPorId(Long id) {
        return ResponseEntity.ok(service.buscarVendaPorId(id));
    }

    @Override
    public ResponseEntity<Void> desfazerVenda(Long id) {
        service.desfazerVenda(id);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<VendaDetalheRepresentation> buscarDetalhesVendaPorId(Long id) {
        return ResponseEntity.ok(service.buscarDetalhesVendaPorId(id));
    }

    @Override
    public ResponseEntity<List<VendaRepresentation>> listarVendasPorCaixaId(Long id) {
        return ResponseEntity.ok(service.listarVendasPorCaixaId(id));
    }

    @Override
    public ResponseEntity<List<ItemVendaProdutoRepresentation>> getTopVendidos(Integer limit) {
        return ResponseEntity.ok(service.getTopVendidos(limit));
    }

    @Override
    public ResponseEntity<Object> listarVendasPaginado(Integer page, Integer size, List<String> sort, LocalDate dataInicial, LocalDate dataFinal) {
        return ResponseEntity.ok(service.listarVendasPaginado(page, size, sort, dataInicial, dataFinal));
    }

    @Override
    public ResponseEntity<VendaStats> getVendasTotaisPorPeriodo(String periodo) {
        return ResponseEntity.ok(service.getVendasTotaisPorPeriodo(periodo));
    }

    @Override
    public ResponseEntity<VendaStats> getVendasQuantidadePorPeriodo(String periodo) {
        return ResponseEntity.ok(service.getVendasQuantidadePorPeriodo(periodo));
    }

    @Override
    public ResponseEntity<VendaStats> getTicketMedioPorPeriodo(String periodo) {
        return ResponseEntity.ok(service.getTicketMedioPorPeriodo(periodo));
    }
}
