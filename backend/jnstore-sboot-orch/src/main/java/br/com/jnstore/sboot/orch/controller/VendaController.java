package br.com.jnstore.sboot.orch.controller;

import br.com.jnstore.sboot.atom.vendas.api.VendasApi;
import br.com.jnstore.sboot.atom.vendas.model.VendaInput;
import br.com.jnstore.sboot.atom.vendas.model.VendaRepresentation;
import br.com.jnstore.sboot.orch.service.VendaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class VendaController implements VendasApi {

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
}
