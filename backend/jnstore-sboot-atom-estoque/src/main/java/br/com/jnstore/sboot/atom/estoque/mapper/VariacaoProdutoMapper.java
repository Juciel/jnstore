package br.com.jnstore.sboot.atom.estoque.mapper;

import br.com.jnstore.sboot.atom.estoque.domain.TbVariacaoProduto;
import br.com.jnstore.sboot.atom.estoque.model.VariacaoProdutoRepresetation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VariacaoProdutoMapper {

    VariacaoProdutoRepresetation toRepresentation(TbVariacaoProduto entity);
}