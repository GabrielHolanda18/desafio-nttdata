package com.desafio.nttdata.servicoemprestimo.strategy;

import com.desafio.nttdata.servicoemprestimo.dto.EmprestimoRequestDTO;
import com.desafio.nttdata.servicoemprestimo.dto.ScoreResponseDTO;
import com.desafio.nttdata.servicoemprestimo.dto.TipoCliente;
import org.springframework.stereotype.Component;

/**
 * Regra PJ (fins didaticos):
 * - Score minimo exigido: 500 (criterio mais rigoroso que PF)
 * - Valor liberado: o menor entre o valor solicitado e 20% do faturamento anual
 */
@Component
public class RegraAprovacaoPJ implements RegraAprovacao {

    private static final int SCORE_MINIMO = 500;
    private static final double PERCENTUAL_FATURAMENTO = 0.20;

    @Override
    public TipoCliente getTipoSuportado() {
        return TipoCliente.PJ;
    }

    @Override
    public ResultadoAprovacao avaliar(EmprestimoRequestDTO request, ScoreResponseDTO score) {
        if (score.getScore() < SCORE_MINIMO) {
            return new ResultadoAprovacao(false,
                    "Score (" + score.getScore() + ") abaixo do minimo exigido (" + SCORE_MINIMO + ")", null);
        }

        double limiteFaturamento = request.getFaturamentoAnual() * PERCENTUAL_FATURAMENTO;
        double valorAprovado = Math.min(request.getValorSolicitado(), limiteFaturamento);

        return new ResultadoAprovacao(true, "Aprovado com base no score e no faturamento anual", valorAprovado);
    }
}