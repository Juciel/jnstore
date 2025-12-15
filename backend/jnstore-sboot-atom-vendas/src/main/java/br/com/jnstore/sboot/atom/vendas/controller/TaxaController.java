package br.com.jnstore.sboot.atom.vendas.controller;

import br.com.jnstore.sboot.atom.vendas.api.TaxaApi;
import br.com.jnstore.sboot.atom.vendas.mapper.TaxaMapper;
import br.com.jnstore.sboot.atom.vendas.model.TaxaInput;
import br.com.jnstore.sboot.atom.vendas.model.TaxaRepresentation;
import br.com.jnstore.sboot.atom.vendas.service.TaxaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TaxaController implements TaxaApi {

    private final TaxaService service;
    private final TaxaMapper mapper;

    @Override
    public ResponseEntity<TaxaRepresentation> getTaxaPorId(Long id) {
        return ResponseEntity.ok(mapper.toRepresentation(service.getTaxaPorId(id)));
    }

    @Override
    public ResponseEntity<TaxaRepresentation> getTaxaPorNome(String nome) {
        return ResponseEntity.ok(mapper.toRepresentation(service.getTaxaPorNome(nome)));
    }

    @Override
    public ResponseEntity<TaxaRepresentation> inserirTaxa(TaxaInput taxaInput) {
        return ResponseEntity.ok(mapper.toRepresentation(service.inserirTaxa(mapper.toDomain(taxaInput))));
    }

    @Override
    public ResponseEntity<TaxaRepresentation> atualizarTaxa(Long id, TaxaInput taxaInput) {
        return ResponseEntity.ok(mapper.toRepresentation(service.atualizarTaxa(id, mapper.toDomain(taxaInput))));
    }
}
