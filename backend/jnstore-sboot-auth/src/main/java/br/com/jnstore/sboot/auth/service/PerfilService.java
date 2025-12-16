package br.com.jnstore.sboot.auth.service;

import br.com.jnstore.sboot.auth.domain.TbPerfil;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public interface PerfilService {
    TbPerfil criarPerfil(TbPerfil tbPerfil);

    boolean podeEditarProduto(List<GrantedAuthority> grants);
    boolean podeVisualizarProduto(List<GrantedAuthority> grants);
    boolean podeVisualizarValorCompra(List<GrantedAuthority> grants);
    boolean podeVisualizarCategoria(List<GrantedAuthority> grants);
    boolean podeEditarCategoria(List<GrantedAuthority> grants);
    boolean podeAbrirCaixa(List<GrantedAuthority> grants);
    boolean podeVisualizarCaixa(List<GrantedAuthority> grants);
    boolean podeFecharCaixa(List<GrantedAuthority> grants);
    boolean podeRetirarCaixa(List<GrantedAuthority> grants);
    boolean podeEditarVenda(List<GrantedAuthority> grants);
    boolean podeVisualizarVenda(List<GrantedAuthority> grants);
    boolean podeEditarValorVenda(List<GrantedAuthority> grants);
}
