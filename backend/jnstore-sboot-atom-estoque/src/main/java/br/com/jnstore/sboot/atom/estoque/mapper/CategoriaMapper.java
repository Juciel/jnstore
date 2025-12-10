package br.com.jnstore.sboot.atom.estoque.mapper;

import br.com.jnstore.sboot.atom.estoque.domain.TbCategoria;
import br.com.jnstore.sboot.atom.estoque.model.CategoriaRepresetation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoriaMapper {

    CategoriaRepresetation toRepresentation(TbCategoria entity);

    @Mapping(target = "id", ignore = true)
    TbCategoria toEntity(CategoriaRepresetation representation);
}