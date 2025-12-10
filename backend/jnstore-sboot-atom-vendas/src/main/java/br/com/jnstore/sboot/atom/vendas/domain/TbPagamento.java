package br.com.jnstore.sboot.atom.vendas.domain;

import br.com.jnstore.sboot.atom.vendas.domain.enums.FormaPagamento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "TB_PAGAMENTO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TbPagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "VENDA_ID", nullable = false)
    private TbVenda venda;

    @Enumerated(EnumType.STRING)
    @Column(name = "FORMA", nullable = false)
    private FormaPagamento forma;

    @Column(name = "VALOR_PAGO", nullable = false)
    private BigDecimal valorPago;
}
