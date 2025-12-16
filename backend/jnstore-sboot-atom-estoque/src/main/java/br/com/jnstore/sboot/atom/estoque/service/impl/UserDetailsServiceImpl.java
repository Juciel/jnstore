package br.com.jnstore.sboot.atom.estoque.service.impl;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Em um ambiente de microsserviços, o serviço de estoque não deve carregar os usuários do seu próprio banco.
        // As informações de autorização (roles) devem vir do token.
        // Por simplicidade aqui, estamos criando um UserDetails com uma role padrão.
        // A validação real ocorre no JwtAuthenticationFilter, que confia no token já validado.
        if (username == null || username.isEmpty()) {
            throw new UsernameNotFoundException("Username is empty");
        }
        return new User(username, "", Collections.emptyList());
    }
}
