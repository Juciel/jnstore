package br.com.jnstore.sboot.atom.vendas.repository;

import br.com.jnstore.sboot.atom.vendas.domain.TbItemVenda;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemVendaRepository extends JpaRepository<TbItemVenda, Long> {

    @Query("SELECT iv.varianteId, SUM(iv.quantidade) AS totalQuantidade " +
           "FROM TbItemVenda iv " +
           "GROUP BY iv.varianteId " +
           "ORDER BY totalQuantidade DESC")
    List<Object[]> findTopSellingItems(Pageable pageable);
}
