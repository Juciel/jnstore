package br.com.jnstore.sboot.atom.vendas.repository;

import br.com.jnstore.sboot.atom.vendas.domain.TbPagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagamentoRepository extends JpaRepository<TbPagamento, Long> {
}
