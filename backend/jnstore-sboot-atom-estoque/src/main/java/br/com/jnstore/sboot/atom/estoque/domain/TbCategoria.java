package br.com.jnstore.sboot.atom.estoque.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "TB_CATEGORIA")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TbCategoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String descricao;

    @Column(name = "USUARIO_CRIACAO", nullable = false)
    private String usuarioCriacao;

    @Column(name = "DATA_CRIACAO", nullable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "USUARIO_ATUALIZACAO")
    private String usuarioAtualizacao;

    @Column(name = "DATA_ATUALIZACAO")
    private LocalDateTime dataAtualizacao;

}