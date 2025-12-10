package br.com.jnstore.sboot.orch.service;

import br.com.jnstore.sboot.atom.estoque.model.ProdutoRepresetation;
import br.com.jnstore.sboot.atom.estoque.model.VariacaoProdutoRepresetation;
import br.com.jnstore.sboot.atom.vendas.model.VendaRepresentation;
import br.com.jnstore.sboot.orch.client.produto.ProdutoClient;
import br.com.jnstore.sboot.orch.client.venda.VendaClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoClient produtoClient;
    private final CategoriaService categoriaService;
    private final VendaClient vendaClient;

    public List<ProdutoRepresetation> getAll(){
        return produtoClient.getAll();
    }

    public ProdutoRepresetation getById(Long id){
        ProdutoRepresetation produtoRepresetation = produtoClient.getById(id);
        if (produtoRepresetation == null) {
            throw new IllegalArgumentException("Produto não encontrado.");
        }
        return produtoRepresetation;
    }

    public ProdutoRepresetation create(ProdutoRepresetation produto){
        if (produto.getVariacoes() == null || produto.getVariacoes().isEmpty()) {
            throw new IllegalArgumentException("É necessário adicionar pelo menos uma variação ao produto.");
        }

        produto.setCategoria(categoriaService.buscarCategoriaPorId(produto.getCategoria().getId()));
        return produtoClient.create(produto);
    }

    public ProdutoRepresetation update(Long id, ProdutoRepresetation produtoNovo){
        if (produtoNovo.getVariacoes() == null || produtoNovo.getVariacoes().isEmpty()) {
            throw new IllegalArgumentException("É necessário adicionar pelo menos uma variação ao produto.");
        }

        ProdutoRepresetation produtoAtual = getById(id);

        Set<Long> idsAtuais = produtoAtual.getVariacoes().stream()
                .map(VariacaoProdutoRepresetation::getId)
                .collect(Collectors.toSet());

        Set<Long> idsNovos = produtoNovo.getVariacoes().stream()
                .map(VariacaoProdutoRepresetation::getId)
                .collect(Collectors.toSet());

        idsAtuais.removeAll(idsNovos);

        if (!idsAtuais.isEmpty()) {
            validarExclusaoVariacoes(idsAtuais.stream().toList());
        }

        return produtoClient.update(id, produtoNovo);
    }

    public void delete(Long id){
        ProdutoRepresetation produtoRepresetation = getById(id);
        if (produtoRepresetation.getVariacoes() != null && !produtoRepresetation.getVariacoes().isEmpty()) {
            List<Long> idVariacoes = produtoRepresetation.getVariacoes().stream()
                    .map(VariacaoProdutoRepresetation::getId)
                    .collect(Collectors.toList());
            validarExclusaoVariacoes(idVariacoes);
        }
        produtoClient.delete(id);
    }

    private void validarExclusaoVariacoes(List<Long> idVariacoes) {
        if (idVariacoes != null && !idVariacoes.isEmpty()) {
            List<VendaRepresentation> vendas = vendaClient.listarVendasPorIdVariacao(idVariacoes);
            if (vendas != null && !vendas.isEmpty()) {
                throw new IllegalArgumentException("Não é possível remover variações de um produto que já possui vendas associadas.");
            }
        }
    }

    public String getNewSku() {
        return produtoClient.getNewSku();
    }

    public List<ProdutoRepresetation> autocomplete(String termo) {
        return produtoClient.autocomplete(termo);
    }
}
