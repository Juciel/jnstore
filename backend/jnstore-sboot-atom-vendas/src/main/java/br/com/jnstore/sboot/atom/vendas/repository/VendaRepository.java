package br.com.jnstore.sboot.atom.vendas.repository;

import br.com.jnstore.sboot.atom.vendas.domain.TbVenda;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VendaRepository extends JpaRepository<TbVenda, Long> {
    @Query("SELECT DISTINCT v FROM TbVenda v JOIN v.itens i WHERE i.varianteId IN :idVariacoes")
    List<TbVenda> listarVendasPorIdVariacao(@Param("idVariacoes") List<Long> idVariacoes);

    List<TbVenda> findByCaixaId(Long caixaId);

    Page<TbVenda> searchByDataVendaBetween(LocalDateTime inicioDoDia, LocalDateTime fimDoDia, PageRequest pageable);
}
