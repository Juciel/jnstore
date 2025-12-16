package br.com.jnstore.sboot.auth.controller;

import br.com.jnstore.sboot.atom.auth.api.UsuariosApi;
import br.com.jnstore.sboot.atom.auth.model.AtualizarSenhaRequest;
import br.com.jnstore.sboot.atom.auth.model.UsuarioRepresentation;
import br.com.jnstore.sboot.auth.mapper.UsuarioMapper;
import br.com.jnstore.sboot.auth.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UsuarioController implements UsuariosApi {

    private final UsuarioService service;
    private final UsuarioMapper mapper;

    @Override
    public ResponseEntity<UsuarioRepresentation> registrarUsuario(UsuarioRepresentation request) {
        var usuario = mapper.toRepresentation(service.registrar(mapper.toDomain(request)));
        return new ResponseEntity<>(usuario, HttpStatus.CREATED);
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
        service.atualizarSenha(request.getNomeUsuario(), request.getNovaSenha());
        return ResponseEntity.noContent().build();
    }
}
