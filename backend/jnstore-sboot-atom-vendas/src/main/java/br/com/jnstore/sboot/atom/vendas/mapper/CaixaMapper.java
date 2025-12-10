package br.com.jnstore.sboot.atom.vendas.mapper;

import br.com.jnstore.sboot.atom.vendas.domain.TbCaixa;
import br.com.jnstore.sboot.atom.vendas.model.CaixaInput;
import br.com.jnstore.sboot.atom.vendas.model.CaixaRepresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CaixaMapper {

    @Mapping(target = "dataAbertura", expression = "java(entity.getDataAbertura() != null ? entity.getDataAbertura().atOffset(java.time.ZoneOffset.UTC) : null)")
    @Mapping(target = "dataFechamento", expression = "java(entity.getDataFechamento() != null ? entity.getDataFechamento().atOffset(java.time.ZoneOffset.UTC) : null)")
    CaixaRepresentation toRepresentation(TbCaixa entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataAbertura", ignore = true)
    @Mapping(target = "dataFechamento", ignore = true)
    @Mapping(target = "valorFinal", ignore = true)
    @Mapping(source = "valor", target = "valorInicial")
    TbCaixa toDomain(CaixaInput input);

    List<CaixaRepresentation> toRepresetationList(List<TbCaixa> tbCaixas);
}
