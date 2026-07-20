package com.desafio.nttdata.servicoemprestimo.strategy;

import com.desafio.nttdata.servicoemprestimo.dto.EmprestimoRequestDTO;
import com.desafio.nttdata.servicoemprestimo.dto.ScoreResponseDTO;
import com.desafio.nttdata.servicoemprestimo.dto.TipoCliente;
import org.springframework.stereotype.Component;

/**
 * Regra PF (fins didaticos):
 * - Score minimo exigido: 400
 * - Valor liberado: o menor entre o valor solicitado e 10x a renda mensal
 */
@Component
public class RegraAprovacaoPF implements RegraAprovacao {

    private static final int SCORE_MINIMO = 400;
    private static final int MULTIPLICADOR_RENDA = 10;

    @Override
    public TipoCliente getTipoSuportado() {
        return TipoCliente.PF;
    }

    @Override
    public ResultadoAprovacao avaliar(EmprestimoRequestDTO request, ScoreResponseDTO score) {
        if (score.getScore() < SCORE_MINIMO) {
            return new ResultadoAprovacao(false,
                    "Score (" + score.getScore() + ") abaixo do minimo exigido (" + SCORE_MINIMO + ")", null);
        }

        double limiteRenda = request.getRendaMensal() * MULTIPLICADOR_RENDA;
        double valorAprovado = Math.min(request.getValorSolicitado(), limiteRenda);

        return new ResultadoAprovacao(true, "Aprovado com base no score e na renda informada", valorAprovado);
    }
}