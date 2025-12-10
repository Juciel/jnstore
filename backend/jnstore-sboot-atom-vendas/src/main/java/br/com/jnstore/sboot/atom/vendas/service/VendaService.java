package br.com.jnstore.sboot.atom.vendas.service;

import br.com.jnstore.sboot.atom.vendas.domain.TbVenda;
import br.com.jnstore.sboot.atom.vendas.model.VendaInput;

import java.util.List;
import java.util.Optional;

public interface VendaService {
    TbVenda registrarVenda(VendaInput domain);
    Optional<TbVenda> buscarVendaPorId(Long id);
    List<TbVenda> listarVendas();
    void desfazerVenda(Long vendaId);
    List<TbVenda> listarVendasPorIdVariacao(List<Long> idVariacao);
}
