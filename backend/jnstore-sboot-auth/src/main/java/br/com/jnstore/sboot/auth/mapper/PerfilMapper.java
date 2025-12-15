package br.com.jnstore.sboot.auth.mapper;

import br.com.jnstore.sboot.atom.auth.model.PerfilRepresentation;
import br.com.jnstore.sboot.auth.domain.TbPerfil;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PerfilMapper {
    TbPerfil toDomain(PerfilRepresentation perfilRepresentation);
    PerfilRepresentation toRepresentation(TbPerfil tbPerfil);
}
