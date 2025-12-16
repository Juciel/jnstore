package br.com.jnstore.sboot.atom.vendas.mapper;

import br.com.jnstore.sboot.atom.vendas.domain.TbCaixa;
import br.com.jnstore.sboot.atom.vendas.model.CaixaRepresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CaixaMapper {

    @Mapping(target = "dataAbertura", expression = "java(entity.getDataAbertura() != null ? entity.getDataAbertura().atOffset(java.time.ZoneOffset.UTC) : null)")
    @Mapping(target = "dataFechamento", expression = "java(entity.getDataFechamento() != null ? entity.getDataFechamento().atOffset(java.time.ZoneOffset.UTC) : null)")
    @Mapping(target = "dataRetirada", expression = "java(entity.getDataRetirada() != null ? entity.getDataRetirada().atOffset(java.time.ZoneOffset.UTC) : null)")
    CaixaRepresentation toRepresentation(TbCaixa entity);

    List<CaixaRepresentation> toRepresetationList(List<TbCaixa> tbCaixas);
}
