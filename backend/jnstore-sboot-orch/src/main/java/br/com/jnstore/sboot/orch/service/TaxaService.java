package br.com.jnstore.sboot.orch.service;

import br.com.jnstore.sboot.atom.vendas.model.TaxaInput;
import br.com.jnstore.sboot.atom.vendas.model.TaxaRepresentation;
import br.com.jnstore.sboot.orch.client.venda.TaxaClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaxaService {

    private final TaxaClient taxaClient;

    public TaxaRepresentation getTaxaPorId(Long id) {
        return taxaClient.getTaxaPorId(id);
    }

    public TaxaRepresentation getTaxaPorNome(String nome) {
        return taxaClient.getTaxaPorNome(nome);
    }

    public TaxaRepresentation inserirTaxa(TaxaInput taxaInput) {
        return taxaClient.inserirTaxa(taxaInput);
    }

    public TaxaRepresentation atualizarTaxa(Long id, TaxaInput taxaInput) {
        return taxaClient.atualizarTaxa(id, taxaInput);
    }
}
