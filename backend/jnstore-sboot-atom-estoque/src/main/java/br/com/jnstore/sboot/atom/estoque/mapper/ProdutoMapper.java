package br.com.jnstore.sboot.atom.estoque.mapper;

import br.com.jnstore.sboot.atom.estoque.domain.TbProduto;
import br.com.jnstore.sboot.atom.estoque.domain.TbVariacaoProduto;
import br.com.jnstore.sboot.atom.estoque.model.ProdutoRepresetation;
import br.com.jnstore.sboot.atom.estoque.model.VariacaoProdutoRepresetation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProdutoMapper {

    ProdutoRepresetation toRepresetation(TbProduto domain);

    List<ProdutoRepresetation> toRepresetationList(List<TbProduto> domainList);

    TbProduto toDomain(ProdutoRepresetation model);

    List<TbProduto> toDomainList(List<ProdutoRepresetation> modelList);

    @Mapping(target = "produto", ignore = true)
    TbVariacaoProduto variacaoToDomain(VariacaoProdutoRepresetation model);
}
