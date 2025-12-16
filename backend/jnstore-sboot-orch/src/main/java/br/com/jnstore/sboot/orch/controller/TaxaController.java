package br.com.jnstore.sboot.orch.controller;

import br.com.jnstore.sboot.atom.vendas.api.TaxaApi;
import br.com.jnstore.sboot.atom.vendas.model.TaxaInput;
import br.com.jnstore.sboot.atom.vendas.model.TaxaRepresentation;
import br.com.jnstore.sboot.orch.service.TaxaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TaxaController implements TaxaApi {

    private final TaxaService service;

    @Override
    public ResponseEntity<TaxaRepresentation> getTaxaPorId(Long id) {
        return ResponseEntity.ok(service.getTaxaPorId(id));
    }

    @Override
    public ResponseEntity<TaxaRepresentation> getTaxaPorNome(String nome) {
        return ResponseEntity.ok(service.getTaxaPorNome(nome));
    }

    @Override
    public ResponseEntity<TaxaRepresentation> inserirTaxa(TaxaInput taxaInput) {
        return ResponseEntity.ok(service.inserirTaxa(taxaInput));
    }

    @Override
    public ResponseEntity<TaxaRepresentation> atualizarTaxa(Long id, TaxaInput taxaInput) {
        return ResponseEntity.ok(service.atualizarTaxa(id, taxaInput));
    }
}
