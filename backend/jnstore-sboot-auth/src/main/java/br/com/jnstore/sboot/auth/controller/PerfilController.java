package br.com.jnstore.sboot.auth.controller;

import br.com.jnstore.sboot.atom.auth.api.PerfilApi;
import br.com.jnstore.sboot.atom.auth.model.PerfilRepresentation;
import br.com.jnstore.sboot.auth.mapper.PerfilMapper;
import br.com.jnstore.sboot.auth.service.PerfilService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

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
}
