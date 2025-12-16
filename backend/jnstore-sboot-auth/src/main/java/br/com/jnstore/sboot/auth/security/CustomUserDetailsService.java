package br.com.jnstore.sboot.auth.security;

import br.com.jnstore.sboot.auth.domain.TbUsuario;
import br.com.jnstore.sboot.auth.repository.UsuarioRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String nomeUsuario) throws UsernameNotFoundException {
        TbUsuario usuario = usuarioRepository.findByNomeUsuario(nomeUsuario)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o nomeUsuario: " + nomeUsuario));

        return new User(usuario.getNomeUsuario(), usuario.getSenha(),
                usuario.getPerfis().stream()
                        .map(perfil -> new SimpleGrantedAuthority("ROLE_" + perfil.getNome()))
                        .collect(Collectors.toList()));
    }
}
