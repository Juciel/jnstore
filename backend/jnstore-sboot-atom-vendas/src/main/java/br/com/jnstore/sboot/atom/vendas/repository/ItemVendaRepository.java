package br.com.jnstore.sboot.atom.vendas.repository;

import br.com.jnstore.sboot.atom.vendas.domain.TbItemVenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemVendaRepository extends JpaRepository<TbItemVenda, Long> {
}
