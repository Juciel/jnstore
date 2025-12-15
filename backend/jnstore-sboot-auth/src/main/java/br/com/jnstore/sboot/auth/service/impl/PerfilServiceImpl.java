package br.com.jnstore.sboot.auth.service.impl;

import br.com.jnstore.sboot.auth.domain.TbPerfil;
import br.com.jnstore.sboot.auth.repository.PerfilRepository;
import br.com.jnstore.sboot.auth.service.PerfilService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PerfilServiceImpl implements PerfilService {

    private final PerfilRepository perfilRepository;

    @Override
    public TbPerfil criarPerfil(TbPerfil perfil) {
        perfil.setNome(perfil.getNome().toUpperCase());
        perfilRepository.findByNome(perfil.getNome())
                .ifPresent(p -> {
                    throw new IllegalArgumentException("Perfil com nome '" + perfil.getNome() + "' já existe.");
                });
        return perfilRepository.save(perfil);
    }

}
