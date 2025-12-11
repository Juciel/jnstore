package br.com.jnstore.sboot.atom.estoque.domain;

import br.com.jnstore.sboot.atom.estoque.domain.enums.Genero;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "TB_PRODUTO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TbProduto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(length = 200)
    private String descricao;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valorCompra;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valorVenda;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private TbCategoria categoria;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Genero genero;

    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TbVariacaoProduto> variacoes;

    @Column(nullable = false)
    private Integer quantidadeTotalEstoque = 0;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valorTotalCompra;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valorTotalVenda;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valorTotalLucro;

    @Column(name = "ID_USUARIO_CRIACAO", nullable = false)
    private Long idUsuarioCriacao;

    @Column(name = "DATA_CRIACAO", nullable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "ID_USUARIO_ATUALIZACAO")
    private Long idUsuarioAtualizacao;

    @Column(name = "DATA_ATUALIZACAO")
    private LocalDateTime dataAtualizacao;

}
