package com.desafio.nttdata.servicoemprestimo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmprestimoResponseDTO {

    private String documento;
    private TipoCliente tipoCliente;
    private double valorSolicitado;
    private int score;
    private String classificacaoScore;
    private boolean aprovado;
    private String motivo;
    private Double valorAprovado; // null quando reprovado
}
