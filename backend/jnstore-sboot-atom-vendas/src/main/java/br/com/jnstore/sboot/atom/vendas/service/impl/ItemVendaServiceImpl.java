package br.com.jnstore.sboot.atom.vendas.service.impl;

import br.com.jnstore.sboot.atom.vendas.repository.ItemVendaRepository;
import br.com.jnstore.sboot.atom.vendas.service.ItemVendaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemVendaServiceImpl implements ItemVendaService {

    private final ItemVendaRepository repository;
}
