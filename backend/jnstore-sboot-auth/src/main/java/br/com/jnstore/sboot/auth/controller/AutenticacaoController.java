package br.com.jnstore.sboot.auth.controller;

import br.com.jnstore.sboot.atom.auth.api.AutenticacaoApi;
import br.com.jnstore.sboot.atom.auth.model.CredenciaisLoginRepresetation;
import br.com.jnstore.sboot.atom.auth.model.TokenResponseRepresetation;
import br.com.jnstore.sboot.atom.auth.model.UsuarioRepresentation;
import br.com.jnstore.sboot.auth.mapper.UsuarioMapper;
import br.com.jnstore.sboot.auth.repository.UsuarioRepository;
import br.com.jnstore.sboot.auth.service.AutenticacaoService;
import br.com.jnstore.sboot.auth.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AutenticacaoController implements AutenticacaoApi {

    private final AutenticacaoService autenticacaoService;
    private final UsuarioService usuarioService;
    private final UsuarioMapper usuarioMapper;


    @Override
    public ResponseEntity<TokenResponseRepresetation> login(CredenciaisLoginRepresetation credenciaisLogin) {
        return ResponseEntity.ok(autenticacaoService.autenticar(credenciaisLogin));
    }

    @Override
    public ResponseEntity<UsuarioRepresentation> getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            // Isso não deve acontecer em um endpoint protegido, mas é uma boa prática de defesa
            return ResponseEntity.status(401).build();
        }
        String username = authentication.getName();
        return usuarioService.findByNomeUsuario(username)
                .map(usuarioMapper::toRepresentationSemSenha)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário '" + username + "' não encontrado no banco de dados, mas presente no token."));
    }
}
