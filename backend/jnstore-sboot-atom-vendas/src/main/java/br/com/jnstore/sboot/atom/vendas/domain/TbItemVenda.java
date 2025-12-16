package br.com.jnstore.sboot.atom.vendas.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "TB_ITEM_VENDA")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TbItemVenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "VENDA_ID", nullable = false)
    private TbVenda venda;

    @Column(name = "VARIANTE_ID", nullable = false)
    private Long varianteId;

    @Column(name = "PRECO_UNITARIO", nullable = false)
    private BigDecimal precoUnitario;

    @Column(name = "QUANTIDADE", nullable = false)
    private Integer quantidade;
}
