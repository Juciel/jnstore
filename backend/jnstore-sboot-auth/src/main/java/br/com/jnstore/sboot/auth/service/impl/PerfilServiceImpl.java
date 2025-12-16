package br.com.jnstore.sboot.auth.service.impl;

import br.com.jnstore.sboot.auth.domain.PerfilNomeEnum;
import br.com.jnstore.sboot.auth.domain.TbPerfil;
import br.com.jnstore.sboot.auth.repository.PerfilRepository;
import br.com.jnstore.sboot.auth.service.PerfilService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;

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

    private boolean checkPermission(List<GrantedAuthority> grants, Predicate<String> permissionRule) {
        if (grants == null) {
            return false;
        }
        return grants.stream()
                .map(GrantedAuthority::getAuthority)
                .map(authority -> authority.startsWith("ROLE_") ? authority.substring(5) : authority)
                .anyMatch(permissionRule);
    }

    private static Predicate<String> allPodeVisualizar() {
        return nome -> PerfilNomeEnum.ADMIN.name().equalsIgnoreCase(nome) || PerfilNomeEnum.GERENTE.name().equalsIgnoreCase(nome) || PerfilNomeEnum.VENDEDOR.name().equalsIgnoreCase(nome);
    }

    private static Predicate<String> allPodeEditar() {
        return nome -> PerfilNomeEnum.ADMIN.name().equalsIgnoreCase(nome) || PerfilNomeEnum.GERENTE.name().equalsIgnoreCase(nome) || PerfilNomeEnum.VENDEDOR.name().equalsIgnoreCase(nome);
    }

    @Override
    public boolean podeEditarProduto(List<GrantedAuthority> grants) {
        return checkPermission(grants, nome -> PerfilNomeEnum.ADMIN.name().equalsIgnoreCase(nome) || PerfilNomeEnum.GERENTE.name().equalsIgnoreCase(nome));
    }

    @Override
    public boolean podeVisualizarProduto(List<GrantedAuthority> grants) {
        return checkPermission(grants, allPodeVisualizar());
    }

    @Override
    public boolean podeVisualizarValorCompra(List<GrantedAuthority> grants) {
        return checkPermission(grants, nome -> PerfilNomeEnum.ADMIN.name().equalsIgnoreCase(nome) || PerfilNomeEnum.GERENTE.name().equalsIgnoreCase(nome));
    }

    @Override
    public boolean podeVisualizarCategoria(List<GrantedAuthority> grants) {
        return checkPermission(grants, allPodeVisualizar());
    }

    @Override
    public boolean podeEditarCategoria(List<GrantedAuthority> grants) {
        return checkPermission(grants, allPodeEditar());
    }

    @Override
    public boolean podeAbrirCaixa(List<GrantedAuthority> grants) {
        return checkPermission(grants, nome -> PerfilNomeEnum.ADMIN.name().equalsIgnoreCase(nome) || PerfilNomeEnum.GERENTE.name().equalsIgnoreCase(nome) || PerfilNomeEnum.VENDEDOR.name().equalsIgnoreCase(nome));
    }

    @Override
    public boolean podeVisualizarCaixa(List<GrantedAuthority> grants) {
        return checkPermission(grants, nome -> PerfilNomeEnum.ADMIN.name().equalsIgnoreCase(nome) || PerfilNomeEnum.GERENTE.name().equalsIgnoreCase(nome) || PerfilNomeEnum.VENDEDOR.name().equalsIgnoreCase(nome));
    }

    @Override
    public boolean podeFecharCaixa(List<GrantedAuthority> grants) {
        return checkPermission(grants, nome -> PerfilNomeEnum.ADMIN.name().equalsIgnoreCase(nome) || PerfilNomeEnum.GERENTE.name().equalsIgnoreCase(nome) || PerfilNomeEnum.VENDEDOR.name().equalsIgnoreCase(nome));
    }

    @Override
    public boolean podeRetirarCaixa(List<GrantedAuthority> grants) {
        return checkPermission(grants, nome -> PerfilNomeEnum.ADMIN.name().equalsIgnoreCase(nome) || PerfilNomeEnum.GERENTE.name().equalsIgnoreCase(nome));
    }

    @Override
    public boolean podeEditarVenda(List<GrantedAuthority> grants) {
        return checkPermission(grants, nome -> PerfilNomeEnum.ADMIN.name().equalsIgnoreCase(nome) || PerfilNomeEnum.GERENTE.name().equalsIgnoreCase(nome) || PerfilNomeEnum.VENDEDOR.name().equalsIgnoreCase(nome));
    }

    @Override
    public boolean podeVisualizarVenda(List<GrantedAuthority> grants) {
        return checkPermission(grants, allPodeVisualizar());
    }

    @Override
    public boolean podeEditarValorVenda(List<GrantedAuthority> grants) {
        return checkPermission(grants, nome -> PerfilNomeEnum.ADMIN.name().equalsIgnoreCase(nome) || PerfilNomeEnum.GERENTE.name().equalsIgnoreCase(nome));
    }
}
