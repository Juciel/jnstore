package br.com.jnstore.sboot.orch.client.auth;

import br.com.jnstore.sboot.auth.model.CredenciaisLoginRepresetation;
import br.com.jnstore.sboot.auth.model.TokenResponseRepresetation;
import br.com.jnstore.sboot.auth.model.UsuarioRepresentation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "authJnstore", url = "${feign.client.auth.url}")
public interface AutenticacaoClient {

    @PostMapping("/api/auth/login")
    TokenResponseRepresetation login(@RequestBody() CredenciaisLoginRepresetation credenciaisLogin);

    @GetMapping("/api/auth/me")
    UsuarioRepresentation getAuthenticatedUser();
}
