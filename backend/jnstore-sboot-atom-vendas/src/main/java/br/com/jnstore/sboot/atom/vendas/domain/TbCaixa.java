package br.com.jnstore.sboot.atom.vendas.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "TB_CAIXA")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TbCaixa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "USUARIO_ID", nullable = false)
    private Long usuarioId;

    @Column(name = "DATA_ABERTURA", nullable = false)
    private LocalDateTime dataAbertura;

    @Column(name = "VALOR_INICIAL", nullable = false)
    private BigDecimal valorInicial;

    @Column(name = "DATA_FECHAMENTO")
    private LocalDateTime dataFechamento;

    @Column(name = "VALOR_FINAL")
    private BigDecimal valorFinal;
}
