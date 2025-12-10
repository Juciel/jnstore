package br.com.jnstore.sboot.atom.estoque.repository;

import br.com.jnstore.sboot.atom.estoque.domain.TbCategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<TbCategoria, Long> {

    Optional<TbCategoria> findByDescricaoIgnoreCase(String descricao);
}
