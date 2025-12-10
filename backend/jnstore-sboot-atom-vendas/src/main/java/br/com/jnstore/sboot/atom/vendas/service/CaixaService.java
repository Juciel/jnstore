package br.com.jnstore.sboot.atom.vendas.service;

import br.com.jnstore.sboot.atom.vendas.domain.TbCaixa;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CaixaService {

    TbCaixa abrirCaixa(TbCaixa domain);
    TbCaixa fecharCaixa(Long id, BigDecimal valorFinal);
    List<TbCaixa> listarCaixas();
    Optional<TbCaixa> buscarCaixaPorId(Long id);
    Optional<TbCaixa> consultaCaixaAbertoHoje();
}
