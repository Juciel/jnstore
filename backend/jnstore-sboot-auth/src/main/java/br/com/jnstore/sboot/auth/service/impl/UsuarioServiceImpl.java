package br.com.jnstore.sboot.auth.service.impl;

import br.com.jnstore.sboot.auth.domain.TbPerfil;
import br.com.jnstore.sboot.auth.domain.TbUsuario;
import br.com.jnstore.sboot.auth.repository.PerfilRepository;
import br.com.jnstore.sboot.auth.repository.UsuarioRepository;
import br.com.jnstore.sboot.auth.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PerfilRepository perfilRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public TbUsuario registrar(TbUsuario novoUsuario) {
        if (usuarioRepository.findByNomeUsuario(novoUsuario.getNomeUsuario()).isPresent()) {
            throw new NoSuchElementException("Usuario já cadastrado");
        }

        List<TbPerfil> perfils = new ArrayList<>();
        novoUsuario.getPerfis().forEach(perfil -> {
            perfils.add(perfilRepository.findByNome(perfil.getNome())
                    .orElseThrow(() -> new NoSuchElementException("Perfil " +perfil.getNome()+ " não encontrado")));
        });

        novoUsuario.setPerfis(perfils);
        novoUsuario.setSenha(passwordEncoder.encode(novoUsuario.getSenha()));

        return usuarioRepository.save(novoUsuario);
    }

    @Override
    public void excluirUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new NoSuchElementException("Usuário com ID " + id + " não encontrado.");
        }
        usuarioRepository.deleteById(id);
    }
}
