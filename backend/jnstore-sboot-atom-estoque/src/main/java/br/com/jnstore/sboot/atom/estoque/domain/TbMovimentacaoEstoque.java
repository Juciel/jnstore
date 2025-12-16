package br.com.jnstore.sboot.atom.estoque.domain;

import br.com.jnstore.sboot.atom.estoque.domain.enums.TipoMovimentacao;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "TB_MOVIMENTACAO_ESTOQUE")
@Data
public class TbMovimentacaoEstoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variacao_produto_id", nullable = false)
    private TbVariacaoProduto variacaoProduto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoMovimentacao tipoMovimentacao;

    @Column(nullable = false)
    private Integer quantidade; // Quantidade de itens movidos

    @Column(name = "data_movimentacao", nullable = false)
    private LocalDateTime dataMovimentacao = LocalDateTime.now(); // Data e hora do registro

    @Column(length = 100)
    private String descricaoMotivo; // NF, ID da Venda, ou descrição do motivo

    @Column(name = "USUARIO_CRIACAO", nullable = false)
    private String usuarioCriacao;
}
