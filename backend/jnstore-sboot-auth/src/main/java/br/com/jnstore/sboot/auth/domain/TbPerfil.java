package br.com.jnstore.sboot.auth.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PERFIL")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TbPerfil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_perfil", unique = true, nullable = false)
    private String nome;

}
