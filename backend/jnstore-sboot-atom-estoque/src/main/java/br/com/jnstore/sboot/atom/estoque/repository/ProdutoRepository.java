package br.com.jnstore.sboot.atom.estoque.repository;

import br.com.jnstore.sboot.atom.estoque.domain.TbProduto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProdutoRepository extends JpaRepository<TbProduto, Long> {

    Optional<TbProduto> findTopByOrderByIdDesc();

    @Query("SELECT DISTINCT p FROM TbProduto p " +
            "LEFT JOIN p.categoria c " +
            "LEFT JOIN p.variacoes v " +
            "WHERE LOWER(p.nome) LIKE LOWER(CONCAT('%', :termo, '%')) " +
            "OR LOWER(c.descricao) LIKE LOWER(CONCAT('%', :termo, '%')) " +
            "OR LOWER(v.identificador) LIKE LOWER(CONCAT('%', :termo, '%'))")
    List<TbProduto> searchByTerm(@Param("termo") String termo, Pageable pageable);

    Optional<TbProduto> findByCategoriaId(Long id);
}
