package br.com.jnstore.sboot.atom.vendas.controller;

import br.com.jnstore.sboot.atom.vendas.api.VendaApi;
import br.com.jnstore.sboot.atom.vendas.mapper.ItemVendaMapper;
import br.com.jnstore.sboot.atom.vendas.mapper.VendaMapper;
import br.com.jnstore.sboot.atom.vendas.model.ItemVendaRepresentation;
import br.com.jnstore.sboot.atom.vendas.model.VendaInput;
import br.com.jnstore.sboot.atom.vendas.model.VendaRepresentation;
import br.com.jnstore.sboot.atom.vendas.model.VendaStats;
import br.com.jnstore.sboot.atom.vendas.service.ItemVendaService;
import br.com.jnstore.sboot.atom.vendas.service.VendaService;
import br.com.jnstore.sboot.atom.vendas.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class VendaController implements VendaApi {

    private final VendaService service;
    private final ItemVendaService itemVendaService;
    private final VendaMapper mapper;
    private final ItemVendaMapper itemVendaMapper;

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
    public ResponseEntity<Object> listarVendasPaginado(Integer page, Integer size, List<String> sort, LocalDate dataInicial, LocalDate dataFinal) {
        // Usa a utilidade para construir o objeto Sort
        Sort sortObject = PaginationUtil.createSortFromStrings(sort);

        // Cria o Pageable com os parâmetros de paginação e ordenação
        Pageable pageable = PageRequest.of(page, size, sortObject);

        return ResponseEntity.ok(service.listarPaginado(pageable, dataInicial, dataFinal));
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

    @Override
    public ResponseEntity<List<ItemVendaRepresentation>> getTopVendidos(Integer limit) {
        return ResponseEntity.ok(itemVendaMapper.toRepresetationList(itemVendaService.getTopVendidos(limit)));
    }
}
