package br.com.jnstore.sboot.auth.mapper;

import br.com.jnstore.sboot.atom.auth.model.UsuarioRepresentation;
import br.com.jnstore.sboot.auth.domain.TbUsuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    @Mapping(target = "primeiroLogin", ignore = true)
    TbUsuario toDomain(UsuarioRepresentation registroUsuario);

    UsuarioRepresentation toRepresentation(TbUsuario registrar);

    @Mapping(target = "senha", ignore = true)
    UsuarioRepresentation toRepresentationSemSenha(TbUsuario registrar);
}
