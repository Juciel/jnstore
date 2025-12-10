package br.com.jnstore.sboot.atom.estoque.service.impl;

import br.com.jnstore.sboot.atom.estoque.domain.TbProduto;
import br.com.jnstore.sboot.atom.estoque.domain.TbVariacaoProduto;
import br.com.jnstore.sboot.atom.estoque.repository.ProdutoRepository;
import br.com.jnstore.sboot.atom.estoque.repository.VariacaoProdutoRepository;
import br.com.jnstore.sboot.atom.estoque.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class ProdutoServiceImpl implements ProdutoService {

    private final ProdutoRepository repository;
    private final VariacaoProdutoRepository variacaoProdutoRepository;

    @Override
    public List<TbProduto> getAll() {
        return repository.findAll();
    }

    @Override
    public Optional<TbProduto> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public TbProduto create(TbProduto produto) {
        atualizarValoresTotaisProduto(produto);
        return repository.save(produto);
    }

    @Override
    @Transactional
    public TbProduto update(TbProduto produto) {
        atualizarValoresTotaisProduto(produto);
       return repository.save(produto);
    }

    @Override
    @Transactional
    public void delete(TbProduto produto) {
        repository.delete(produto);
    }

    @Override
    public String getNewSku() {
        long lastIdProduto = repository.findTopByOrderByIdDesc().map(TbProduto::getId).orElse(0L);
        long produtoId = lastIdProduto + 1;

        long lastIdVariacao = variacaoProdutoRepository.findTopByOrderByIdDesc().map(TbVariacaoProduto::getId).orElse(0L);
        long variacaoId = lastIdVariacao + 1;

        String newSku;
        do {
            long random = ThreadLocalRandom.current().nextLong(1000, 10000);
            newSku = String.format("%d-%d-%d", produtoId, variacaoId, random);
        } while (variacaoProdutoRepository.existsByIdentificador(newSku));

        return newSku;
    }

    @Override
    public List<TbProduto> autocomplete(String termo) {
        return repository.searchByTerm(termo, PageRequest.of(0, 5));
    }

    private static void atualizarValoresTotaisProduto(TbProduto produto) {
        int quantidadeEstoqueTotal = 0;
        if (produto.getVariacoes() != null) {
            for (TbVariacaoProduto variacaoProduto : produto.getVariacoes()) {
                variacaoProduto.setProduto(produto);
                quantidadeEstoqueTotal += variacaoProduto.getQuantidadeEstoque() != null ? variacaoProduto.getQuantidadeEstoque() : 0;
            }
        }
        produto.setQuantidadeTotalEstoque(quantidadeEstoqueTotal);

        BigDecimal quantidadeTotalBD = new BigDecimal(quantidadeEstoqueTotal);
        BigDecimal valorTotalCompra = BigDecimal.ZERO;
        BigDecimal valorTotalVenda = BigDecimal.ZERO;

        if (produto.getValorCompra() != null) {
            valorTotalCompra = produto.getValorCompra().multiply(quantidadeTotalBD);
        }
        if (produto.getValorVenda() != null) {
            valorTotalVenda = produto.getValorVenda().multiply(quantidadeTotalBD);
        }

        BigDecimal valorTotalLucro = valorTotalVenda.subtract(valorTotalCompra);

        produto.setValorTotalCompra(valorTotalCompra);
        produto.setValorTotalVenda(valorTotalVenda);
        produto.setValorTotalLucro(valorTotalLucro);
    }
}
