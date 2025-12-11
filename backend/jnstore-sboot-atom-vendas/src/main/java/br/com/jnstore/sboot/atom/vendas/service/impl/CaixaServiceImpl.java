package br.com.jnstore.sboot.atom.vendas.service.impl;

import br.com.jnstore.sboot.atom.vendas.domain.TbCaixa;
import br.com.jnstore.sboot.atom.vendas.domain.enums.StatusCaixa;
import br.com.jnstore.sboot.atom.vendas.mapper.CaixaMapper;
import br.com.jnstore.sboot.atom.vendas.model.CaixaRepresentation;
import br.com.jnstore.sboot.atom.vendas.repository.CaixaRepository;
import br.com.jnstore.sboot.atom.vendas.service.CaixaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable; // Alterado para Pageable
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CaixaServiceImpl implements CaixaService {

    private final CaixaRepository repository;
    private final CaixaMapper mapper;

    @Override
    @Transactional
    public TbCaixa abrirCaixa(BigDecimal valorInicial){
        TbCaixa tbCaixa = new TbCaixa();
        tbCaixa.setIdUsuarioAbertura(1L);
        tbCaixa.setDataAbertura(LocalDateTime.now());
        tbCaixa.setValorInicial(valorInicial);
        tbCaixa.setStatus(StatusCaixa.ABERTO);
        return repository.save(tbCaixa);
    }

    @Override
    @Transactional
    public TbCaixa fecharCaixa(Long id, BigDecimal valorTotalVendas) {
        TbCaixa tbCaixa = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Caixa não encontrado"));
        tbCaixa.setIdUsuarioFechamento(1L);
        tbCaixa.setDataFechamento(LocalDateTime.now());
        tbCaixa.setValorFinal(valorTotalVendas);
        tbCaixa.setStatus(StatusCaixa.FECHADO);
        return repository.save(tbCaixa);
    }

    @Override
    @Transactional
    public TbCaixa retiradaCaixa(Long id, BigDecimal valorRetirada) {
        TbCaixa tbCaixa = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Caixa não encontrado"));
        tbCaixa.setIdUsuarioRetirada(1L);
        tbCaixa.setDataRetirada(LocalDateTime.now());
        tbCaixa.setValorRetirada(valorRetirada);
        tbCaixa.setStatus(StatusCaixa.RETIRADO);
        return repository.save(tbCaixa);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TbCaixa> listarCaixas() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TbCaixa> buscarCaixaPorId(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<TbCaixa> consultaCaixaAbertoHoje() {
        LocalDate hoje = LocalDate.now();
        LocalDateTime inicioDoDia = hoje.atStartOfDay();
        LocalDateTime fimDoDia = hoje.atTime(LocalTime.MAX);

        return repository.findByDataAberturaBetweenAndDataFechamentoIsNull(inicioDoDia, fimDoDia);
    }

    @Override
    public Page<CaixaRepresentation> listarPaginado(Pageable pageable, LocalDate dataInicial, LocalDate dataFinal) {
        Page<TbCaixa> pageResult;
        if (dataInicial != null && dataFinal != null) {
            LocalDateTime inicioDoDia = dataInicial.atStartOfDay();
            LocalDateTime fimDoDia = dataFinal.atTime(LocalTime.MAX);
            pageResult = repository.findByDataAberturaBetween(inicioDoDia, fimDoDia, pageable);
        } else {
            pageResult = repository.findAll(pageable);
        }
        return pageResult.map(mapper::toRepresentation);
    }
}
