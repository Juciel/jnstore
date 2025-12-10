package br.com.jnstore.sboot.atom.estoque.service.impl;

import br.com.jnstore.sboot.atom.estoque.domain.TbMovimentacaoEstoque;
import br.com.jnstore.sboot.atom.estoque.domain.TbProduto;
import br.com.jnstore.sboot.atom.estoque.domain.TbVariacaoProduto;
import br.com.jnstore.sboot.atom.estoque.domain.enums.TipoMovimentacao;
import br.com.jnstore.sboot.atom.estoque.model.MovimentacaoEstoqueInput;
import br.com.jnstore.sboot.atom.estoque.repository.MovimentacaoEstoqueRepository;
import br.com.jnstore.sboot.atom.estoque.repository.ProdutoRepository;
import br.com.jnstore.sboot.atom.estoque.repository.VariacaoProdutoRepository;
import br.com.jnstore.sboot.atom.estoque.service.MovimentacaoEstoqueService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class MovimentacaoEstoqueServiceImpl implements MovimentacaoEstoqueService {

    private final MovimentacaoEstoqueRepository repository;
    private final VariacaoProdutoRepository variacaoProdutoRepository;
    private final ProdutoRepository produtoRepository;

    @Override
    @Transactional
    public void registrarEntrada(MovimentacaoEstoqueInput movimentacaoEstoqueInput) {
        movimentacaoEstoqueInput.getItens().forEach(item -> {
            TbVariacaoProduto variacao = variacaoProdutoRepository.findById(item.getIdVariacao())
                    .orElseThrow(() -> new NoSuchElementException("Variação de produto com ID " + item.getIdVariacao() + " não encontrada."));

            // Atualiza a quantidade em estoque na variação
            int novaQuantidade = variacao.getQuantidadeEstoque() + item.getQuantidade();
            variacao.setQuantidadeEstoque(novaQuantidade);
            variacaoProdutoRepository.save(variacao);

            // Atualiza a quantidade total no produto pai
            TbProduto produto = variacao.getProduto();
            produto.setQuantidadeTotalEstoque(produto.getQuantidadeTotalEstoque() + item.getQuantidade());
            produtoRepository.save(produto);

            // Cria e salva o registro da movimentação
            TbMovimentacaoEstoque movimentacao = new TbMovimentacaoEstoque();
            movimentacao.setVariacaoProduto(variacao);
            movimentacao.setTipoMovimentacao(TipoMovimentacao.ENTRADA);
            movimentacao.setQuantidade(item.getQuantidade());
            movimentacao.setDataMovimentacao(LocalDateTime.now());
            movimentacao.setDescricaoMotivo(movimentacaoEstoqueInput.getDescricaoMotivo());
            movimentacao.setUsuarioId(movimentacaoEstoqueInput.getUsuarioId());

            repository.save(movimentacao);
        });
    }

    @Override
    @Transactional
    public void registrarSaida(MovimentacaoEstoqueInput movimentacaoEstoqueInput) {
        movimentacaoEstoqueInput.getItens().forEach(item -> {
            TbVariacaoProduto variacao = variacaoProdutoRepository.findById(item.getIdVariacao())
                    .orElseThrow(() -> new NoSuchElementException("Variação de produto com ID " + item.getIdVariacao() + " não encontrada."));

            if (variacao.getQuantidadeEstoque() < item.getQuantidade()) {
                throw new IllegalArgumentException("Estoque insuficiente para a variação " + variacao.getIdentificador() + ". Saldo atual: " + variacao.getQuantidadeEstoque() + ".");
            }

            int novaQuantidade = variacao.getQuantidadeEstoque() - item.getQuantidade();
            variacao.setQuantidadeEstoque(novaQuantidade);
            variacaoProdutoRepository.save(variacao);

            // Atualiza a quantidade total no produto pai
            TbProduto produto = variacao.getProduto();
            produto.setQuantidadeTotalEstoque(produto.getQuantidadeTotalEstoque() - item.getQuantidade());
            produtoRepository.save(produto);

            TbMovimentacaoEstoque movimentacao = new TbMovimentacaoEstoque();
            movimentacao.setVariacaoProduto(variacao);
            movimentacao.setTipoMovimentacao(TipoMovimentacao.SAIDA);
            movimentacao.setQuantidade(item.getQuantidade());
            movimentacao.setDataMovimentacao(LocalDateTime.now());
            movimentacao.setDescricaoMotivo(movimentacaoEstoqueInput.getDescricaoMotivo());
            movimentacao.setUsuarioId(movimentacaoEstoqueInput.getUsuarioId());

            repository.save(movimentacao);
        });
    }
}