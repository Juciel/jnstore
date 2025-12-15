package br.com.jnstore.sboot.atom.vendas.service.impl;

import br.com.jnstore.sboot.atom.vendas.domain.TbCaixa;
import br.com.jnstore.sboot.atom.vendas.domain.TbVenda;
import br.com.jnstore.sboot.atom.vendas.domain.enums.StatusCaixa;
import br.com.jnstore.sboot.atom.vendas.mapper.VendaMapper;
import br.com.jnstore.sboot.atom.vendas.model.VendaInput;
import br.com.jnstore.sboot.atom.vendas.model.VendaRepresentation;
import br.com.jnstore.sboot.atom.vendas.model.VendaStats;
import br.com.jnstore.sboot.atom.vendas.repository.CaixaRepository;
import br.com.jnstore.sboot.atom.vendas.repository.VendaRepository;
import br.com.jnstore.sboot.atom.vendas.service.VendaService;
import br.com.jnstore.sboot.atom.vendas.util.CalculationUtil;
import br.com.jnstore.sboot.atom.vendas.util.DateCalculationUtil;
import br.com.jnstore.sboot.atom.vendas.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
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
public class VendaServiceImpl implements VendaService {

    private final VendaRepository repository;
    private final CaixaRepository caixaRepository;
    private final VendaMapper mapper;

    @Override
    @Transactional
    public TbVenda registrarVenda(VendaInput vendaInput) {
        TbCaixa tbCaixa = caixaRepository.findById(vendaInput.getCaixaId())
                .orElseThrow(() -> new NoSuchElementException("Caixa não encontrado"));

        TbVenda tbVenda = mapper.toDomain(vendaInput);
        tbVenda.setCaixa(tbCaixa);
        tbVenda.setDataVenda(LocalDateTime.now());

        if(tbVenda.getTotalBruto() == null || tbVenda.getTotalLiquido() == null) {
            BigDecimal totalBruto = vendaInput.getItens().stream()
                    .map(item -> item.getPrecoUnitario().multiply(new BigDecimal(item.getQuantidade())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            tbVenda.setTotalBruto(totalBruto);

            BigDecimal desconto = vendaInput.getDesconto() != null ? vendaInput.getDesconto() : BigDecimal.ZERO;
            tbVenda.setTotalLiquido(totalBruto.subtract(desconto));
        }

        // Associa a venda aos itens e pagamentos
        tbVenda.getItens().forEach(item -> item.setVenda(tbVenda));
        tbVenda.getPagamentos().forEach(pagamento -> pagamento.setVenda(tbVenda));

        // Atualiza o valor final do caixa
        BigDecimal valorFinalCaixa = tbCaixa.getValorFinal() != null ? tbCaixa.getValorFinal() : BigDecimal.ZERO;
        tbCaixa.setValorFinal(valorFinalCaixa.add(tbVenda.getTotalLiquido()));
        caixaRepository.save(tbCaixa);

        tbVenda.setIdUsuarioVenda(1L);
        return repository.save(tbVenda);
    }

    @Override
    @Transactional
    public void desfazerVenda(Long vendaId) {
        TbVenda tbVenda = repository.findById(vendaId)
                .orElseThrow(() -> new NoSuchElementException("Venda não encontrada com o ID: " + vendaId));

        TbCaixa tbCaixa = tbVenda.getCaixa();
        if (tbCaixa == null) {
            throw new IllegalArgumentException("Venda SEM caixa associado, não é possível desfazer.");
        }

        if (StatusCaixa.FECHADO.equals(tbCaixa.getStatus()) || StatusCaixa.RETIRADO.equals(tbCaixa.getStatus())) {
            throw new IllegalArgumentException("Venda com caixa "+tbCaixa.getStatus()+", não é possível desfazer.");
        }

        // Reverte o valor final do caixa
        BigDecimal valorFinalCaixa = tbCaixa.getValorFinal() != null ? tbCaixa.getValorFinal() : BigDecimal.ZERO;
        tbCaixa.setValorFinal(valorFinalCaixa.subtract(tbVenda.getTotalLiquido()));
        caixaRepository.save(tbCaixa);

        repository.delete(tbVenda);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TbVenda> listarVendasPorIdVariacao(List<Long> idVariacao) {
        return repository.listarVendasPorIdVariacao(idVariacao);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TbVenda> listarVendasPorCaixaId(Long caixaId) {
        return repository.findByCaixaId(caixaId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VendaRepresentation> listarPaginado(Pageable pageable, LocalDate dataInicial, LocalDate dataFinal) {
        // Aplica a ordenação padrão por ID descendente se a paginação não tiver ordenação
        Pageable sortedPageable = PaginationUtil.applyDefaultSortIfUnsorted(pageable, "id");

        Page<TbVenda> pageResult;
        if (dataInicial != null && dataFinal != null) {
            LocalDateTime inicioDoDia = dataInicial.atStartOfDay();
            LocalDateTime fimDoDia = dataFinal.atTime(LocalTime.MAX);
            pageResult = repository.searchByDataVendaBetween(inicioDoDia, fimDoDia, sortedPageable);
        } else {
            pageResult = repository.findAll(sortedPageable);
        }
        return pageResult.map(mapper::toRepresentation);
    }

    @Override
    @Transactional(readOnly = true)
    public VendaStats getVendasTotaisPorPeriodo(String periodo) {
        Pair<LocalDateTime, LocalDateTime> currentPeriodDates = DateCalculationUtil.getPeriodDates(periodo, 0);
        BigDecimal currentTotal = repository.sumTotalLiquidoByDataVendaBetween(currentPeriodDates.getFirst(), currentPeriodDates.getSecond());
        currentTotal = currentTotal != null ? currentTotal : BigDecimal.ZERO;

        Pair<LocalDateTime, LocalDateTime> previousPeriodDates = DateCalculationUtil.getPeriodDates(periodo, -1);
        BigDecimal previousTotal = repository.sumTotalLiquidoByDataVendaBetween(previousPeriodDates.getFirst(), previousPeriodDates.getSecond());
        previousTotal = previousTotal != null ? previousTotal : BigDecimal.ZERO;

        VendaStats vendaStats = new VendaStats();
        vendaStats.setTotal(currentTotal);
        vendaStats.setVariacao(CalculationUtil.calculateVariation(currentTotal, previousTotal));
        return vendaStats;
    }

    @Override
    @Transactional(readOnly = true)
    public VendaStats getVendasQuantidadePorPeriodo(String periodo) {
        Pair<LocalDateTime, LocalDateTime> currentPeriodDates = DateCalculationUtil.getPeriodDates(periodo, 0);
        Long currentQuantity = repository.countByDataVendaBetween(currentPeriodDates.getFirst(), currentPeriodDates.getSecond());
        currentQuantity = currentQuantity != null ? currentQuantity : 0L;

        Pair<LocalDateTime, LocalDateTime> previousPeriodDates = DateCalculationUtil.getPeriodDates(periodo, -1);
        Long previousQuantity = repository.countByDataVendaBetween(previousPeriodDates.getFirst(), previousPeriodDates.getSecond());
        previousQuantity = previousQuantity != null ? previousQuantity : 0L;

        VendaStats vendaStats = new VendaStats();
        vendaStats.setTotal(BigDecimal.valueOf(currentQuantity));
        vendaStats.setVariacao(CalculationUtil.calculateVariation(BigDecimal.valueOf(currentQuantity), BigDecimal.valueOf(previousQuantity)));
        return vendaStats;
    }

    @Override
    @Transactional(readOnly = true)
    public VendaStats getTicketMedioPorPeriodo(String periodo) {
        Pair<LocalDateTime, LocalDateTime> currentPeriodDates = DateCalculationUtil.getPeriodDates(periodo, 0);
        BigDecimal currentTicketMedio = repository.avgTotalLiquidoByDataVendaBetween(currentPeriodDates.getFirst(), currentPeriodDates.getSecond());
        currentTicketMedio = currentTicketMedio != null ? currentTicketMedio : BigDecimal.ZERO;

        Pair<LocalDateTime, LocalDateTime> previousPeriodDates = DateCalculationUtil.getPeriodDates(periodo, -1);
        BigDecimal previousTicketMedio = repository.avgTotalLiquidoByDataVendaBetween(previousPeriodDates.getFirst(), previousPeriodDates.getSecond());
        previousTicketMedio = previousTicketMedio != null ? previousTicketMedio : BigDecimal.ZERO;

        VendaStats vendaStats = new VendaStats();
        vendaStats.setTotal(currentTicketMedio);
        vendaStats.setVariacao(CalculationUtil.calculateVariation(currentTicketMedio, previousTicketMedio));
        return vendaStats;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TbVenda> listarVendas() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TbVenda> buscarVendaPorId(Long id) {
        return repository.findById(id);
    }
}
