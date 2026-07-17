package com.desafio.nttdata.servicoscore.strategy;

import com.desafio.nttdata.servicoscore.dto.ScoreRequestDTO;
import com.desafio.nttdata.servicoscore.dto.TipoCliente;
import org.springframework.stereotype.Component;

/**
 * Regra de score para Pessoa Fisica.
 *
 * Formula simplificada (fins didaticos):
 * - Renda mensal contribui ate 600 pontos (renda / 10, limitado a 600)
 * - Idade contribui ate 400 pontos (idade * 8, limitado a 400),
 *   pois consideramos que clientes mais maduros tem historico mais estavel
 */

@Component
public class RegraScorePF implements RegraScore {

    private static final double PESO_RENDA = 10.0;
    private static final int TETO_RENDA = 600;
    private static final int PESO_IDADE = 8;
    private static final int TETO_IDADE = 400;


    @Override
    public TipoCliente getTipoSuportado() {
        return TipoCliente.PF;
    }

    @Override
    public int calcularScore(ScoreRequestDTO request) {

        double pontosRenda = Math.min(request.getRendaMensal() / PESO_RENDA, TETO_RENDA);
        double pontosIdade = Math.min(request.getIdade() * PESO_IDADE, TETO_IDADE);

        int scoreFinal = (int) Math.round(pontosRenda + pontosIdade);
        return Math.min(scoreFinal, 1000);
    }
}
