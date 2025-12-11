package br.com.jnstore.sboot.atom.vendas.service;

import br.com.jnstore.sboot.atom.vendas.domain.TbItemVenda;

import java.util.List;

public interface ItemVendaService {

    List<TbItemVenda> getTopVendidos(Integer limit);
}
