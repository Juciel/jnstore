package br.com.jnstore.sboot.atom.estoque.repository;

import br.com.jnstore.sboot.atom.estoque.domain.TbMovimentacaoEstoque;
import br.com.jnstore.sboot.atom.estoque.domain.TbVariacaoProduto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovimentacaoEstoqueRepository extends JpaRepository<TbMovimentacaoEstoque, Long> {
}