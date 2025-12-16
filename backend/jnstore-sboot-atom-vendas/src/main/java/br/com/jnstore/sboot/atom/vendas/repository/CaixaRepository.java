package br.com.jnstore.sboot.atom.vendas.repository;

import br.com.jnstore.sboot.atom.vendas.domain.TbCaixa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface CaixaRepository extends JpaRepository<TbCaixa, Long> {
    Optional<TbCaixa> findByDataAberturaBetweenAndDataFechamentoIsNull(LocalDateTime inicioDoDia, LocalDateTime fimDoDia);

    Page<TbCaixa> findByDataAberturaBetween(LocalDateTime inicioDoDia, LocalDateTime fimDoDia, Pageable pageable);
}
