package br.com.jnstore.sboot.atom.vendas.mapper;

import br.com.jnstore.sboot.atom.vendas.domain.TbVenda;
import br.com.jnstore.sboot.atom.vendas.model.VendaInput;
import br.com.jnstore.sboot.atom.vendas.model.VendaRepresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ItemVendaMapper.class, PagamentoMapper.class})
public interface VendaMapper {

    @Mapping(source = "caixa.id", target = "caixaId")
    @Mapping(target = "dataVenda", expression = "java(entity.getDataVenda() != null ? entity.getDataVenda().atOffset(java.time.ZoneOffset.UTC) : null)")
    VendaRepresentation toRepresentation(TbVenda entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "caixa", ignore = true)
    @Mapping(target = "dataVenda", ignore = true)
    @Mapping(target = "usuarioVenda", ignore = true)
    TbVenda toDomain(VendaInput input);

    List<VendaRepresentation> toRepresetationList(List<TbVenda> tbVendas);
}
