package br.com.jnstore.sboot.auth.repository;

import br.com.jnstore.sboot.auth.domain.TbPerfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PerfilRepository extends JpaRepository<TbPerfil, Long> {

    Optional<TbPerfil> findByNome(String nome);
}
