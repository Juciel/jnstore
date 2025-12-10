package br.com.jnstore.sboot.atom.estoque.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "TB_VARIACAO_PRODUTO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TbVariacaoProduto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String identificador;

    @Column(length = 50)
    private String cor;

    @Column(length = 10)
    private String tamanho;

    @Column(nullable = false)
    private Integer quantidadeEstoque = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    private TbProduto produto;

}