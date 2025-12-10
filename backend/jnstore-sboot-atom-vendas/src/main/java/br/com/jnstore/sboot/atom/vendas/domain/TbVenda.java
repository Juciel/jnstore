package br.com.jnstore.sboot.atom.vendas.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "TB_VENDA")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TbVenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "CAIXA_ID", nullable = false)
    private TbCaixa caixa;

    @Column(name = "DATA_VENDA", nullable = false)
    private LocalDateTime dataVenda;

    @Column(name = "TOTAL_BRUTO", nullable = false)
    private BigDecimal totalBruto;

    @Column(name = "DESCONTO")
    private BigDecimal desconto;

    @Column(name = "TOTAL_LIQUIDO", nullable = false)
    private BigDecimal totalLiquido;

    @OneToMany(mappedBy = "venda", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TbItemVenda> itens;

    @OneToMany(mappedBy = "venda", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TbPagamento> pagamentos;
}
