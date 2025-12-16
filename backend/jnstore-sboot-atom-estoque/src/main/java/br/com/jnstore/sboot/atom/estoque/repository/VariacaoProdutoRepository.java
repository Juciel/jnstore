package br.com.jnstore.sboot.atom.estoque.repository;

import br.com.jnstore.sboot.atom.estoque.domain.TbProduto;
import br.com.jnstore.sboot.atom.estoque.domain.TbVariacaoProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VariacaoProdutoRepository extends JpaRepository<TbVariacaoProduto, Long> {

    Optional<TbVariacaoProduto> findTopByOrderByIdDesc();

    boolean existsByIdentificador(String identificador);
}
