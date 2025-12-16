package br.com.jnstore.sboot.orch.controller;

import br.com.jnstore.sboot.auth.api.AutenticacaoApi;
import br.com.jnstore.sboot.auth.model.CredenciaisLoginRepresetation;
import br.com.jnstore.sboot.auth.model.TokenResponseRepresetation;
import br.com.jnstore.sboot.auth.model.UsuarioRepresentation;
import br.com.jnstore.sboot.orch.service.AutenticacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AutenticacaoController implements AutenticacaoApi {

    private final AutenticacaoService service;

    @Override
    public ResponseEntity<TokenResponseRepresetation> login(CredenciaisLoginRepresetation credenciaisLogin) {
        return ResponseEntity.ok(service.login(credenciaisLogin));
    }

    @Override
    public ResponseEntity<UsuarioRepresentation> getAuthenticatedUser() {
        return ResponseEntity.ok(service.getAuthenticatedUser());
    }
}
