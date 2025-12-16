package br.com.jnstore.sboot.atom.estoque.service;

import br.com.jnstore.sboot.atom.estoque.model.MovimentacaoEstoqueInput;

public interface MovimentacaoEstoqueService {

    void registrarEntrada(MovimentacaoEstoqueInput movimentacaoEstoqueInput);

    void registrarSaida(MovimentacaoEstoqueInput movimentacaoEstoqueInput);
}