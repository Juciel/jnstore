package br.com.jnstore.sboot.atom.vendas.service.impl;

import br.com.jnstore.sboot.atom.vendas.domain.TbCaixa;
import br.com.jnstore.sboot.atom.vendas.repository.CaixaRepository;
import br.com.jnstore.sboot.atom.vendas.service.CaixaService;
import lombok.RequiredArgsConstructor;
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

    @Transactional
    public TbCaixa abrirCaixa(TbCaixa tbCaixa){
        tbCaixa.setDataAbertura(LocalDateTime.now());
        return repository.save(tbCaixa);
    }

    @Override
    @Transactional
    public TbCaixa fecharCaixa(Long id, BigDecimal valorTotalVendas) {
        TbCaixa tbCaixa = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Caixa não encontrado"));
        tbCaixa.setDataFechamento(LocalDateTime.now());
        tbCaixa.setValorFinal(valorTotalVendas);
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
}
