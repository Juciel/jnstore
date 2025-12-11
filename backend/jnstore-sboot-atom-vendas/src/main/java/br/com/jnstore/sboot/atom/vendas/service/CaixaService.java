package br.com.jnstore.sboot.atom.vendas.service;

import br.com.jnstore.sboot.atom.vendas.domain.TbCaixa;
import br.com.jnstore.sboot.atom.vendas.model.CaixaRepresentation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CaixaService {

    TbCaixa abrirCaixa(BigDecimal valorInicial);
    TbCaixa fecharCaixa(Long id, BigDecimal valorFinal);
    TbCaixa retiradaCaixa(Long id, BigDecimal valorRtirada);
    List<TbCaixa> listarCaixas();
    Optional<TbCaixa> buscarCaixaPorId(Long id);
    Optional<TbCaixa> consultaCaixaAbertoHoje();
    Page<CaixaRepresentation> listarPaginado(Pageable pageable, LocalDate dataInicial, LocalDate dataFinal);
}
