package com.desafio.nttdata.servicoscore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScoreResponseDTO {

    private String documento;
    private TipoCliente tipoCliente;
    private int score;          // 0 a 1000
    private String classificacao; // BAIXO, MEDIO, ALTO
}
