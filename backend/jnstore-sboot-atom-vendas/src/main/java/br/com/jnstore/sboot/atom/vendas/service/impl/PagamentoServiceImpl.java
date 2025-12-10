package br.com.jnstore.sboot.atom.vendas.service.impl;

import br.com.jnstore.sboot.atom.vendas.repository.PagamentoRepository;
import br.com.jnstore.sboot.atom.vendas.service.PagamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PagamentoServiceImpl implements PagamentoService {

    private final PagamentoRepository repository;
}
