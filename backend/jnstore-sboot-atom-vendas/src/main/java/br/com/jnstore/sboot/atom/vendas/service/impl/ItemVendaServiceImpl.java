package br.com.jnstore.sboot.atom.vendas.service.impl;

import br.com.jnstore.sboot.atom.vendas.domain.TbItemVenda;
import br.com.jnstore.sboot.atom.vendas.repository.ItemVendaRepository;
import br.com.jnstore.sboot.atom.vendas.service.ItemVendaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemVendaServiceImpl implements ItemVendaService {

    private final ItemVendaRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<TbItemVenda> getTopVendidos(Integer limit) {
        if (limit == null || limit <= 0) {
            return List.of();
        }
        Pageable pageable = PageRequest.of(0, limit);
        List<Object[]> topSellingItems = repository.findTopSellingItems(pageable);

        return topSellingItems.stream().map(result -> {
            Long varianteId = (Long) result[0];
            Long totalQuantidade = (Long) result[1];
            TbItemVenda item = new TbItemVenda();
            item.setVarianteId(varianteId);
            item.setQuantidade(totalQuantidade.intValue());
            return item;
        }).collect(Collectors.toList());
    }
}
