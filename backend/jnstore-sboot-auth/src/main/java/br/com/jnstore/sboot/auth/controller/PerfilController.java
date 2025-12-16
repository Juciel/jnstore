package br.com.jnstore.sboot.auth.controller;

import br.com.jnstore.sboot.atom.auth.api.PerfilApi;
import br.com.jnstore.sboot.atom.auth.model.PerfilRepresentation;
import br.com.jnstore.sboot.auth.mapper.PerfilMapper;
import br.com.jnstore.sboot.auth.service.PerfilService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PerfilController implements PerfilApi {

    private final PerfilService service;
    private final PerfilMapper mapper;

    @Override
    public ResponseEntity<PerfilRepresentation> criarPerfil(PerfilRepresentation perfilRepresentation) {
        PerfilRepresentation novoPerfil = mapper.toRepresentation(service.criarPerfil(mapper.toDomain(perfilRepresentation)));
        return new ResponseEntity<>(novoPerfil, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Boolean> podeEditarProduto() {
        return ResponseEntity.ok(service.podeEditarProduto(getGrants()));
    }

    @Override
    public ResponseEntity<Boolean> podeVisualizarProduto() {
        return ResponseEntity.ok(service.podeVisualizarProduto(getGrants()));
    }

    @Override
    public ResponseEntity<Boolean> podeVisualizarValorCompra() {
        return ResponseEntity.ok(service.podeVisualizarValorCompra(getGrants()));
    }

    @Override
    public ResponseEntity<Boolean> podeVisualizarCategoria() {
        return ResponseEntity.ok(service.podeVisualizarCategoria(getGrants()));
    }

    @Override
    public ResponseEntity<Boolean> podeEditarCategoria() {
        return ResponseEntity.ok(service.podeEditarCategoria(getGrants()));
    }

    @Override
    public ResponseEntity<Boolean> podeAbrirCaixa() {
        return ResponseEntity.ok(service.podeAbrirCaixa(getGrants()));
    }

    @Override
    public ResponseEntity<Boolean> podeVisualizarCaixa() {
        return ResponseEntity.ok(service.podeVisualizarCaixa(getGrants()));
    }

    @Override
    public ResponseEntity<Boolean> podeFecharCaixa() {
        return ResponseEntity.ok(service.podeFecharCaixa(getGrants()));
    }

    @Override
    public ResponseEntity<Boolean> podeRetirarCaixa() {
        return ResponseEntity.ok(service.podeRetirarCaixa(getGrants()));
    }

    @Override
    public ResponseEntity<Boolean> podeEditarVenda() {
        return ResponseEntity.ok(service.podeEditarVenda(getGrants()));
    }

    @Override
    public ResponseEntity<Boolean> podeVisualizarVenda() {
        return ResponseEntity.ok(service.podeVisualizarVenda(getGrants()));
    }

    @Override
    public ResponseEntity<Boolean> podeEditarValorVenda() {
        return ResponseEntity.ok(service.podeEditarValorVenda(getGrants()));
    }

    private List<GrantedAuthority> getGrants(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || CollectionUtils.isEmpty(authentication.getAuthorities())) {
            // Isso não deve acontecer em um endpoint protegido, mas é uma boa prática de defesa
            return null;
        }
        return (List<GrantedAuthority>) authentication.getAuthorities();
    }
}
