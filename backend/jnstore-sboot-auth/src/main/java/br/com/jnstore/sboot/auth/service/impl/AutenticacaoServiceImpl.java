package br.com.jnstore.sboot.auth.service.impl;

import br.com.jnstore.sboot.atom.auth.model.CredenciaisLoginRepresetation;
import br.com.jnstore.sboot.atom.auth.model.TokenResponseRepresetation;
import br.com.jnstore.sboot.auth.security.JwtTokenProvider;
import br.com.jnstore.sboot.auth.service.AutenticacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AutenticacaoServiceImpl implements AutenticacaoService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public TokenResponseRepresetation autenticar(CredenciaisLoginRepresetation credenciais) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        credenciais.getNomeUsuario(),
                        credenciais.getSenha()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtTokenProvider.generateToken(authentication);
        TokenResponseRepresetation token = new TokenResponseRepresetation();
        token.setToken(jwt);
        token.setTipo("Bearer");
        token.setExpiresIn(3600000);
        return token;
    }
}
