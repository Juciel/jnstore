package br.com.jnstore.sboot.atom.vendas.service;

import br.com.jnstore.sboot.atom.vendas.domain.TbTaxas;

public interface TaxaService {

    TbTaxas getTaxaPorNome(String nome);
    TbTaxas getTaxaPorId(Long id);
    TbTaxas inserirTaxa(TbTaxas tbTaxas);
    TbTaxas atualizarTaxa(Long id, TbTaxas tbTaxas);
}
