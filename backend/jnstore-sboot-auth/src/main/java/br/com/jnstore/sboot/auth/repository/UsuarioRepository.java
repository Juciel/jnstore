package br.com.jnstore.sboot.auth.repository;

import br.com.jnstore.sboot.auth.domain.TbUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<TbUsuario, Long> {

    Optional<TbUsuario> findByNomeUsuario(String nomeUsuario);

    boolean existsByNomeUsuario(String nomeUsuario);
}
