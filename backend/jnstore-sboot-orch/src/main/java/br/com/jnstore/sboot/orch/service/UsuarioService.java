package br.com.jnstore.sboot.orch.service;

import br.com.jnstore.sboot.auth.model.AtualizarSenhaRequest;
import br.com.jnstore.sboot.auth.model.UsuarioRepresentation;
import br.com.jnstore.sboot.orch.client.auth.UsuarioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioClient client;

    public UsuarioRepresentation registrarUsuario(UsuarioRepresentation request) {
        return client.registrarUsuario(request);
    }

    public void excluirUsuario(Long id) {
        client.excluirUsuario(id);
    }

    public Boolean isPrimeiroLogin(String nomeUsuario) {
        return client.isPrimeiroLogin(nomeUsuario);
    }

    public void atualizarSenha(AtualizarSenhaRequest request) {
        client.atualizarSenha(request);
    }
}
