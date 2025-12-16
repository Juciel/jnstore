package br.com.jnstore.sboot.atom.vendas.mapper;

import br.com.jnstore.sboot.atom.vendas.domain.TbPagamento;
import br.com.jnstore.sboot.atom.vendas.model.PagamentoInput;
import br.com.jnstore.sboot.atom.vendas.model.PagamentoRepresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface PagamentoMapper {

    PagamentoRepresentation toRepresentation(TbPagamento tbPagamento);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "venda", ignore = true)
    TbPagamento toDomain(PagamentoInput input);
}
