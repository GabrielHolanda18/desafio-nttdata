package com.desafio.nttdata.servicoscore.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScoreRequestDTO {

    @NotNull(message = "O tipo de cliente (PF ou PJ) e obrigatorio")
    private TipoCliente tipoCliente;

    @NotNull(message = "O documento (CPF ou CNPJ) e obrigatorio")
    private String documento;

    // Campos usados quando tipoCliente = PF
    private Integer idade;
    private Double rendaMensal;

    // Campos usados quando tipoCliente = PJ
    private Double faturamentoAnual;
    private Integer tempoMercadoAnos;

}
