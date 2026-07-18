package com.desafio.nttdata.servicoemprestimo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * Espelha exatamente o ScoreRequestDTO do servico-score.
 * O Feign usa esse objeto para montar o corpo JSON da requisição HTTP.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScoreRequestDTO {

    private TipoCliente tipoCliente;
    private String documento;
    private Integer idade;
    private Double rendaMensal;
    private Double faturamentoAnual;
    private Integer tempoMercadoAnos;

}
