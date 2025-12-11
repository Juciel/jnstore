package br.com.jnstore.sboot.atom.vendas.controller;

import br.com.jnstore.sboot.atom.vendas.api.VendasApi;
import br.com.jnstore.sboot.atom.vendas.mapper.VendaMapper;
import br.com.jnstore.sboot.atom.vendas.model.VendaInput;
import br.com.jnstore.sboot.atom.vendas.model.VendaRepresentation;
import br.com.jnstore.sboot.atom.vendas.service.VendaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class VendaController implements VendasApi {

    private final VendaService service;
    private final VendaMapper mapper;

    @Override
    public ResponseEntity<VendaRepresentation> registrarVenda(VendaInput vendaInput) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toRepresentation(service.registrarVenda(vendaInput)));
    }

    @Override
    public ResponseEntity<Void> desfazerVenda(Long idVenda) {
        service.desfazerVenda(idVenda);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<List<VendaRepresentation>> listarVendas() {
        return ResponseEntity.ok(mapper.toRepresetationList(service.listarVendas()));
    }

    @Override
    public ResponseEntity<VendaRepresentation> buscarVendaPorId(Long id) {
        return service.buscarVendaPorId(id)
                .map(mapper::toRepresentation)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<List<VendaRepresentation>> listarVendasPorIdVariacao(List<Long> idVariacao) {
        return ResponseEntity.ok(mapper.toRepresetationList(service.listarVendasPorIdVariacao(idVariacao)));
    }

    @Override
    public ResponseEntity<List<VendaRepresentation>> listarVendasPorCaixaId(Long caixaId) {
        return ResponseEntity.ok(mapper.toRepresetationList(service.listarVendasPorCaixaId(caixaId)));
    }

    @Override
    public ResponseEntity<Object> listarVendasPaginado(Integer page, Integer size, LocalDate dataInicial, LocalDate dataFinal) {
        PageRequest pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(service.listarPaginado(pageable, dataInicial, dataFinal));
    }
}
