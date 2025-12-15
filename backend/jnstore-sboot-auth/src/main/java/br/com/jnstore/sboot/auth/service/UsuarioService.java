package br.com.jnstore.sboot.auth.service;


import br.com.jnstore.sboot.auth.domain.TbUsuario;

public interface UsuarioService {
    TbUsuario registrar(TbUsuario novoUsuario);
    void excluirUsuario(Long id);
}
