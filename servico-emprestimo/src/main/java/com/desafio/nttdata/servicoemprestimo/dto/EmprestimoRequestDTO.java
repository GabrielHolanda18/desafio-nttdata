package com.desafio.nttdata.servicoemprestimo.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmprestimoRequestDTO {

    @NotNull(message = "O tipo de cliente (PF ou PJ) é obrigatório")
    private TipoCliente tipoCliente;

    private String documento;

    @NotNull(message = "O valor solicitado é obrigatório")
    @Positive(message = "O valor solicitado deve ser maior que zero")
    private Double valorSolicitado;

    // Campos usados quando tipoCliente = PF
    private Integer idade;
    private Double rendaMensal;

    // Campos usados quando tipoCliente = PJ
    private Double faturamentoAnual;
    private Integer tempoMercadoAnos;
}
