package br.com.jnstore.sboot.auth.service;

import br.com.jnstore.sboot.atom.auth.model.CredenciaisLoginRepresetation;
import br.com.jnstore.sboot.atom.auth.model.TokenResponseRepresetation;

public interface AutenticacaoService {
    TokenResponseRepresetation autenticar(CredenciaisLoginRepresetation credenciais);
}
