package br.com.jnstore.sboot.auth.service;


import br.com.jnstore.sboot.auth.domain.TbUsuario;

import java.util.Optional;

public interface UsuarioService {
    TbUsuario registrar(TbUsuario novoUsuario);
    void excluirUsuario(Long id);
    Optional<TbUsuario> findByNomeUsuario(String nome);
    boolean isPrimeiroLogin(String nomeUsuario);
    void atualizarSenha(String nomeUsuario, String novaSenha);
}
