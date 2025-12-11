package br.com.jnstore.sboot.atom.vendas.domain;

import br.com.jnstore.sboot.atom.vendas.domain.enums.StatusCaixa;
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

    @Column(name = "ID_USUARIO_ABERTURA", nullable = false)
    private Long idUsuarioAbertura;

    @Column(name = "DATA_ABERTURA", nullable = false)
    private LocalDateTime dataAbertura;

    @Column(name = "VALOR_INICIAL", nullable = false)
    private BigDecimal valorInicial;

    @Column(name = "ID_USUARIO_FECHAMENTO")
    private Long idUsuarioFechamento;

    @Column(name = "DATA_FECHAMENTO")
    private LocalDateTime dataFechamento;

    @Column(name = "VALOR_FINAL")
    private BigDecimal valorFinal;

    @Column(name = "ID_USUARIO_RETIRADA")
    private Long idUsuarioRetirada;

    @Column(name = "DATA_RETIRADA")
    private LocalDateTime dataRetirada;

    @Column(name = "VALOR_RETIRADA")
    private BigDecimal valorRetirada;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false)
    private StatusCaixa status;
}
