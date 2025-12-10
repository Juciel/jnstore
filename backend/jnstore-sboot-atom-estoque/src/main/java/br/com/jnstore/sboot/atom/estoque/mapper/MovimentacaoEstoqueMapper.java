package br.com.jnstore.sboot.atom.estoque.mapper;

import br.com.jnstore.sboot.atom.estoque.domain.TbMovimentacaoEstoque;
import br.com.jnstore.sboot.atom.estoque.model.MovimentacaoEstoqueRepresetation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {VariacaoProdutoMapper.class})
public interface MovimentacaoEstoqueMapper {

    @Mapping(target = "dataMovimentacao", expression = "java(entity.getDataMovimentacao() != null ? entity.getDataMovimentacao().atOffset(java.time.ZoneOffset.UTC) : null)")
    MovimentacaoEstoqueRepresetation toRepresentation(TbMovimentacaoEstoque entity);
}