package br.com.jnstore.sboot.orch.service;

import br.com.jnstore.sboot.atom.vendas.model.CaixaInput;
import br.com.jnstore.sboot.atom.vendas.model.CaixaRepresentation;
import br.com.jnstore.sboot.orch.client.venda.CaixaClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CaixaService {

    private final CaixaClient caixaClient;

    public CaixaRepresentation abrirCaixa(CaixaInput caixaInput) {
        return caixaClient.abrirCaixa(caixaInput);
    }

    public CaixaRepresentation fecharCaixa(Long id, CaixaInput caixaInput) {
        return caixaClient.fecharCaixa(id, caixaInput);
    }

    public List<CaixaRepresentation> listarCaixas() {
        return caixaClient.listarCaixas();
    }

    public CaixaRepresentation buscarCaixaPorId(Long id) {
        return caixaClient.buscarCaixaPorId(id);
    }

    public CaixaRepresentation consultaCaixaAbertoHoje() {
        return caixaClient.consultaCaixaAbertoHoje();
    }

    public Object listarCaixasPaginado(Integer page, Integer size, List<String> sort, LocalDate dataInicial, LocalDate dataFinal) {
        return caixaClient.listarCaixasPaginado(page, size, sort, dataInicial, dataFinal);
    }

    public CaixaRepresentation retiradaCaixa(Long id, CaixaInput caixaInput) {
        return caixaClient.retiradaCaixa(id, caixaInput);
    }
}
