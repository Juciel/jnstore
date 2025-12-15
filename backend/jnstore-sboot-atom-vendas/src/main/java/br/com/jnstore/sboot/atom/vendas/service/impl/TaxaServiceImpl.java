package br.com.jnstore.sboot.atom.vendas.service.impl;

import br.com.jnstore.sboot.atom.vendas.domain.TbTaxas;
import br.com.jnstore.sboot.atom.vendas.repository.TaxaRepository;
import br.com.jnstore.sboot.atom.vendas.service.TaxaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class TaxaServiceImpl implements TaxaService {

    private final TaxaRepository repository;

    @Override
    public TbTaxas getTaxaPorNome(String nome) {
        return repository.findByNomeIgnoreCase(nome).orElseThrow(() -> new NoSuchElementException("Taxa com nome " + nome + " não encontrada."));
    }

    @Override
    public TbTaxas getTaxaPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Taxa com ID " + id + " não encontrada."));
    }

    @Override
    public TbTaxas inserirTaxa(TbTaxas tbTaxas) {
        repository.findByNomeIgnoreCase(tbTaxas.getNome())
                .ifPresent(c -> {
                    throw new IllegalArgumentException("Já existe uma taxa com o nome '" + tbTaxas.getNome() + "'.");
                });
        tbTaxas.setIdUsuarioCriacao(1L);
        tbTaxas.setDataCriacao(LocalDateTime.now());
        return repository.save(tbTaxas);
    }

    @Override
    public TbTaxas atualizarTaxa(Long id, TbTaxas tbTaxas) {
        TbTaxas entity = getTaxaPorId(id);

        repository.findByNomeIgnoreCase(tbTaxas.getNome())
                .ifPresent(c -> {
                    throw new IllegalArgumentException("Já existe uma taxa com o nome '" + tbTaxas.getNome() + "'.");
                });

        entity.setNome(tbTaxas.getNome());
        entity.setIdUsuarioAtualizacao(1L); // Placeholder para usuário logado
        entity.setDataAtualizacao(LocalDateTime.now());
        return repository.save(entity);
    }
}
