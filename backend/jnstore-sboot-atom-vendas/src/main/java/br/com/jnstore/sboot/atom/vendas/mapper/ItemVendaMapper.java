package br.com.jnstore.sboot.atom.vendas.mapper;

import br.com.jnstore.sboot.atom.vendas.domain.TbItemVenda;
import br.com.jnstore.sboot.atom.vendas.model.ItemVendaInput;
import br.com.jnstore.sboot.atom.vendas.model.ItemVendaRepresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ItemVendaMapper {

    ItemVendaRepresentation toRepresentation(TbItemVenda tbItemVenda);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "venda", ignore = true)
    TbItemVenda toDomain(ItemVendaInput input);
}
