package br.com.jnstore.sboot.atom.vendas.repository;

import br.com.jnstore.sboot.atom.vendas.domain.TbTaxas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaxaRepository extends JpaRepository<TbTaxas, Long> {

    Optional<TbTaxas> findByNomeIgnoreCase(String admin);
}
