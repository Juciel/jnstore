package br.com.jnstore.sboot.orch.service;

import br.com.jnstore.sboot.auth.model.CredenciaisLoginRepresetation;
import br.com.jnstore.sboot.auth.model.TokenResponseRepresetation;
import br.com.jnstore.sboot.auth.model.UsuarioRepresentation;
import br.com.jnstore.sboot.orch.client.auth.AutenticacaoClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AutenticacaoService {

    private final AutenticacaoClient client;

    public TokenResponseRepresetation login(CredenciaisLoginRepresetation credenciaisLogin) {
        return client.login(credenciaisLogin);
    }

    public UsuarioRepresentation getAuthenticatedUser() {
        return client.getAuthenticatedUser();
    }
}
