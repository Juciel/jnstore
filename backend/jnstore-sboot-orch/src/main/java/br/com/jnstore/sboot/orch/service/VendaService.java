package br.com.jnstore.sboot.orch.service;

import br.com.jnstore.sboot.atom.estoque.model.ItemMovimentacaoEstoqueRepresentation;
import br.com.jnstore.sboot.atom.estoque.model.MovimentacaoEstoqueInput;
import br.com.jnstore.sboot.atom.vendas.model.*;
import br.com.jnstore.sboot.orch.client.produto.MovimentacaoEstoqueClient;
import br.com.jnstore.sboot.orch.client.venda.CaixaClient;
import br.com.jnstore.sboot.orch.client.venda.VendaClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class VendaService {
    private final VendaClient vendaClient;
    private final CaixaClient caixaClient;
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
}
