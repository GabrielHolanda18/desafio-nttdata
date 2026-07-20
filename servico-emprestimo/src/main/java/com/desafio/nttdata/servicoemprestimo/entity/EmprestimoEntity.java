package com.desafio.nttdata.servicoemprestimo.entity;


import com.desafio.nttdata.servicoemprestimo.dto.TipoCliente;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "emprestimos")
@Data
@NoArgsConstructor
public class EmprestimoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String documento;

    @Enumerated(EnumType.STRING)
    private TipoCliente tipoCliente;

    /*
    * Não usei o @Column nos atributos propositalmente. Sem essa
    * anotação, o Hibernate já infere automaticamente o nome da coluna
     */
    private double valorSolicitado;
    private int score;
    private String classificacaoScore;
    private boolean aprovado;
    private String motivo;
    private Double valorAprovado;

    private LocalDateTime dataAvaliacao;

    public EmprestimoEntity(String documento, TipoCliente tipoCliente, double valorSolicitado,
                            int score, String classificacaoScore, boolean aprovado,
                            String motivo, Double valorAprovado) {
        this.documento = documento;
        this.tipoCliente = tipoCliente;
        this.valorSolicitado = valorSolicitado;
        this.score = score;
        this.classificacaoScore = classificacaoScore;
        this.aprovado = aprovado;
        this.motivo = motivo;
        this.valorAprovado = valorAprovado;
        this.dataAvaliacao = LocalDateTime.now();
    }
}
