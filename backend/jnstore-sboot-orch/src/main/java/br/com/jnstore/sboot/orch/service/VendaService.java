package br.com.jnstore.sboot.orch.service;

import br.com.jnstore.sboot.atom.estoque.model.ItemMovimentacaoEstoqueRepresentation;
import br.com.jnstore.sboot.atom.estoque.model.MovimentacaoEstoqueInput;
import br.com.jnstore.sboot.atom.estoque.model.ProdutoRepresetation;
import br.com.jnstore.sboot.atom.estoque.model.VariacaoProdutoRepresetation;
import br.com.jnstore.sboot.atom.vendas.model.*;
import br.com.jnstore.sboot.orch.client.produto.MovimentacaoEstoqueClient;
import br.com.jnstore.sboot.orch.client.produto.ProdutoClient;
import br.com.jnstore.sboot.orch.client.venda.CaixaClient;
import br.com.jnstore.sboot.orch.client.venda.VendaClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VendaService {
    private final VendaClient vendaClient;
    private final CaixaClient caixaClient;
    private final ProdutoClient produtoClient;
    private final MovimentacaoEstoqueClient movimentacaoEstoqueClient;

    public VendaRepresentation registrarVenda(VendaInput vendaInput) {
        consultaCaixaAbertoHoje(vendaInput);
        registrarSaidaEstoque(vendaInput);
        VendaRepresentation vendaRepresentation = null;
        try {
            vendaRepresentation = vendaClient.registrarVenda(vendaInput);
        } catch (Exception e) {
            registrarEntradaEstoque(vendaInput);
            throw new RuntimeException("Erro ao registrar venda", e);
        }
        return vendaRepresentation;
    }

    private void registrarSaidaEstoque(VendaInput vendaInput) {
        MovimentacaoEstoqueInput estoque = getMovimentacaoEstoqueInput(vendaInput);
        estoque.setDescricaoMotivo("Venda do caixa " + vendaInput.getCaixaId() + " no valor de " + vendaInput.getTotalLiquido());
        movimentacaoEstoqueClient.registrarSaida(estoque);
    }

    private void registrarEntradaEstoque(VendaInput vendaInput) {
        MovimentacaoEstoqueInput estoque = getMovimentacaoEstoqueInput(vendaInput);
        estoque.setDescricaoMotivo("Desfeita a venda do caixa " + vendaInput.getCaixaId() + " no valor de " + vendaInput.getTotalLiquido());
        movimentacaoEstoqueClient.registrarEntrada(estoque);
    }

    private static MovimentacaoEstoqueInput getMovimentacaoEstoqueInput(VendaInput vendaInput) {
        MovimentacaoEstoqueInput estoque = new MovimentacaoEstoqueInput();
        List<ItemMovimentacaoEstoqueRepresentation> itensEstoque = new ArrayList<>();
        vendaInput.getItens().forEach(itemVendaInput -> {
            ItemMovimentacaoEstoqueRepresentation item = new ItemMovimentacaoEstoqueRepresentation();
            item.setIdVariacao(itemVendaInput.getVarianteId());
            item.setQuantidade(itemVendaInput.getQuantidade());
            itensEstoque.add(item);
        });
        estoque.setItens(itensEstoque);
        estoque.setUsuarioId(1L);
        return estoque;
    }

    private void consultaCaixaAbertoHoje(VendaInput vendaInput) {
        try {
            CaixaRepresentation caixaRepresentation = caixaClient.consultaCaixaAbertoHoje();
            if (caixaRepresentation != null) {
                vendaInput.setCaixaId(caixaRepresentation.getId());
            }
        }catch (FeignException.FeignClientException e){
            throw new NoSuchElementException("Caixa do dia não está aberto");
        }
    }

    public List<VendaRepresentation> listarVendas() {
        return vendaClient.listarVendas();
    }

    public VendaRepresentation buscarVendaPorId(Long id) {
        VendaRepresentation vendaRepresentation = vendaClient.buscarVendaPorId(id);
        if (vendaRepresentation == null) {
            throw new IllegalArgumentException("Venda não encontrado.");
        }
        return vendaRepresentation;
    }

    public void desfazerVenda(Long id) {
        VendaRepresentation vendaRepresentation = buscarVendaPorId(id);
        List<ItemVendaInput> itensVendaInput = new ArrayList<>();
        vendaRepresentation.getItens().forEach(itemVenda -> {
            ItemVendaInput itemVendaInput = new ItemVendaInput();
            itemVendaInput.setVarianteId(itemVenda.getVarianteId());
            itemVendaInput.setQuantidade(itemVenda.getQuantidade());
            itensVendaInput.add(itemVendaInput);
        });
        VendaInput vendaInput = new VendaInput();
        vendaInput.setItens(itensVendaInput);
        registrarEntradaEstoque(vendaInput);
        try {
            vendaClient.desfazerVenda(id);
        } catch (Exception e) {
            registrarSaidaEstoque(vendaInput);
            throw new RuntimeException("Erro ao desfazer venda", e);
        }
    }

    public List<VendaRepresentation> listarVendasPorIdVariacao(List<Long> idVariacao){
        return vendaClient.listarVendasPorIdVariacao(idVariacao);
    }

    public VendaDetalheRepresentation buscarDetalhesVendaPorId(Long id) {
        VendaRepresentation vendaRepresentation = vendaClient.buscarVendaPorId(id);
        VendaDetalheRepresentation vendaDetalheRepresentation = new VendaDetalheRepresentation();
        vendaDetalheRepresentation.setId(id);
        vendaDetalheRepresentation.setCaixa(caixaClient.buscarCaixaPorId(vendaRepresentation.getCaixaId()));
        vendaDetalheRepresentation.setDataVenda(vendaRepresentation.getDataVenda());
        vendaDetalheRepresentation.setTotalBruto(vendaRepresentation.getTotalBruto());
        vendaDetalheRepresentation.setDesconto(vendaRepresentation.getDesconto());
        vendaDetalheRepresentation.setTotalLiquido(vendaRepresentation.getTotalLiquido());
        vendaDetalheRepresentation.setPagamentos(vendaRepresentation.getPagamentos());

        List<Long> idVariacoes = vendaRepresentation.getItens().stream()
                .map(ItemVendaRepresentation::getVarianteId)
                .collect(Collectors.toList());

        List<ProdutoRepresetation> produtosComVariacoes = produtoClient.listarProdutosPorIdVariacao(idVariacoes);

        List<ItemVendaProdutoRepresentation> itensDetalhes = getItemVendaProdutoRepresentations(vendaRepresentation.getItens(), produtosComVariacoes);
        vendaDetalheRepresentation.setItens(itensDetalhes);
        return vendaDetalheRepresentation;
    }

    public List<VendaRepresentation> listarVendasPorCaixaId(Long id) {
        return vendaClient.listarVendasPorCaixaId(id);
    }

    public Object listarVendasPaginado(Integer page, Integer size, List<String> sort, LocalDate dataInicial, LocalDate dataFinal) {
        return vendaClient.listarVendasPaginado(page, size, sort, dataInicial, dataFinal);
    }

    public List<ItemVendaProdutoRepresentation> getTopVendidos(Integer limit) {
        List<ItemVendaRepresentation> itens = vendaClient.getTopVendidos(limit);
        List<Long> idVariacoes = itens.stream()
                .map(ItemVendaRepresentation::getVarianteId)
                .collect(Collectors.toList());
        List<ProdutoRepresetation> produtosComVariacoes = produtoClient.listarProdutosPorIdVariacao(idVariacoes);

        return getItemVendaProdutoRepresentations(itens, produtosComVariacoes);
    }

    public VendaStats getVendasTotaisPorPeriodo(String periodo) {
        return vendaClient.getVendasTotaisPorPeriodo(periodo);
    }

    public VendaStats getVendasQuantidadePorPeriodo(String periodo) {
        return vendaClient.getVendasQuantidadePorPeriodo(periodo);
    }

    public VendaStats getTicketMedioPorPeriodo(String periodo) {
        return vendaClient.getTicketMedioPorPeriodo(periodo);
    }

    public static List<ItemVendaProdutoRepresentation> getItemVendaProdutoRepresentations(List<ItemVendaRepresentation> itens, List<ProdutoRepresetation> produtosComVariacoes) {
        List<ItemVendaProdutoRepresentation> itensDetalhes = new ArrayList<>();

        for (ItemVendaRepresentation itemVenda : itens) {
            ItemVendaProdutoRepresentation itemVendaProdutoRepresentation = new ItemVendaProdutoRepresentation();

            itemVendaProdutoRepresentation.setId(itemVenda.getId());
            itemVendaProdutoRepresentation.setPrecoUnitario(itemVenda.getPrecoUnitario());
            itemVendaProdutoRepresentation.setQuantidade(itemVenda.getQuantidade());
            itemVendaProdutoRepresentation.setVarianteId(itemVenda.getVarianteId());

            ProdutoRepresetation produtoAssociado = produtosComVariacoes.stream()
                    .filter(p -> p.getVariacoes().stream()
                            .anyMatch(v -> v.getId().equals(itemVenda.getVarianteId())))
                    .findFirst()
                    .orElse(null);

            if (produtoAssociado != null) {
                VariacaoProdutoRepresetation variacaoAssociada = produtoAssociado.getVariacoes().stream()
                        .filter(v -> v.getId().equals(itemVenda.getVarianteId()))
                        .findFirst()
                        .orElse(null);

                if (variacaoAssociada != null) {
                    itemVendaProdutoRepresentation.setIdProduto(produtoAssociado.getId());
                    itemVendaProdutoRepresentation.setNomeProduto(produtoAssociado.getNome());
                    itemVendaProdutoRepresentation.setDescricaoProduto(produtoAssociado.getDescricao());
                    itemVendaProdutoRepresentation.setDescricaoCategoria(produtoAssociado.getCategoria().getDescricao());
                    itemVendaProdutoRepresentation.setDescricaoGenero(produtoAssociado.getGenero().getValue());

                    itemVendaProdutoRepresentation.setIdentificador(variacaoAssociada.getIdentificador());
                    itemVendaProdutoRepresentation.setCor(variacaoAssociada.getCor());
                    itemVendaProdutoRepresentation.setTamanho(variacaoAssociada.getTamanho());
                }
            }
            itensDetalhes.add(itemVendaProdutoRepresentation);
        }
        return itensDetalhes;
    }

}
