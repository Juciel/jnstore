package br.com.jnstore.sboot.atom.vendas.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "TB_TAXAS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TbTaxas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(name = "VALOR_TAXA", nullable = false)
    private BigDecimal valorTaxa;

    @Column(name = "USUARIO_CRIACAO", nullable = false)
    private String usuarioCriacao;

    @Column(name = "DATA_CRIACAO", nullable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "USUARIO_ATUALIZACAO")
    private String usuarioAtualizacao;

    @Column(name = "DATA_ATUALIZACAO")
    private LocalDateTime dataAtualizacao;
}
