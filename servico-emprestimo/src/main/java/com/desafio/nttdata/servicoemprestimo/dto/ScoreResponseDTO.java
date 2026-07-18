package com.desafio.nttdata.servicoemprestimo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Espelha o ScoreResponseDTO retornado pelo servico-score.
 * E o objeto que o Feign vai desserializar automaticamente a partir
 * do JSON de resposta do POST /score.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScoreResponseDTO {

    private String documento;
    private TipoCliente tipoCliente;
    private int score;
    private String classificacao;
}
