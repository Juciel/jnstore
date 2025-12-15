package br.com.jnstore.sboot.atom.vendas.mapper;

import br.com.jnstore.sboot.atom.vendas.domain.TbTaxas;
import br.com.jnstore.sboot.atom.vendas.model.TaxaInput;
import br.com.jnstore.sboot.atom.vendas.model.TaxaRepresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaxaMapper {

    TaxaRepresentation toRepresentation(TbTaxas tbTaxas);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "idUsuarioCriacao", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "idUsuarioAtualizacao", ignore = true)
    @Mapping(target = "dataAtualizacao", ignore = true)
    TbTaxas toDomain(TaxaInput taxaInput);
}
