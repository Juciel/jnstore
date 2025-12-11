package br.com.jnstore.sboot.atom.vendas.service;

import br.com.jnstore.sboot.atom.vendas.domain.TbVenda;
import br.com.jnstore.sboot.atom.vendas.model.VendaInput;
import br.com.jnstore.sboot.atom.vendas.model.VendaRepresentation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface VendaService {
    TbVenda registrarVenda(VendaInput domain);
    Optional<TbVenda> buscarVendaPorId(Long id);
    List<TbVenda> listarVendas();
    void desfazerVenda(Long vendaId);
    List<TbVenda> listarVendasPorIdVariacao(List<Long> idVariacao);
    List<TbVenda> listarVendasPorCaixaId(Long caixaId);
    Page<VendaRepresentation> listarPaginado(PageRequest pageable, LocalDate dataInicial, LocalDate dataFinal);
}
