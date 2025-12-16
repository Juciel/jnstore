package br.com.jnstore.sboot.orch.controller;

import br.com.jnstore.sboot.auth.api.UsuariosApi;
import br.com.jnstore.sboot.auth.model.AtualizarSenhaRequest;
import br.com.jnstore.sboot.auth.model.UsuarioRepresentation;
import br.com.jnstore.sboot.orch.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UsuarioController implements UsuariosApi {

    private final UsuarioService service;

    @Override
    public ResponseEntity<UsuarioRepresentation> registrarUsuario(UsuarioRepresentation request) {
        return new ResponseEntity<>(service.registrarUsuario(request), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> excluirUsuario(Long id) {
        service.excluirUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Boolean> isPrimeiroLogin(String nomeUsuario) {
        return ResponseEntity.ok(service.isPrimeiroLogin(nomeUsuario));
    }

    @Override
    public ResponseEntity<Void> atualizarSenha(AtualizarSenhaRequest request) {
        service.atualizarSenha(request);
        return ResponseEntity.noContent().build();
    }
}
