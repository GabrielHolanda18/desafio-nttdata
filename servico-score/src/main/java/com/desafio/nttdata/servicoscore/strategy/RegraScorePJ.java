package com.desafio.nttdata.servicoscore.strategy;

import com.desafio.nttdata.servicoscore.dto.ScoreRequestDTO;
import com.desafio.nttdata.servicoscore.dto.TipoCliente;
import org.springframework.stereotype.Component;

/**
 * Regra de score para Pessoa Juridica.
 *
 * Formula simplificada (fins didaticos):
 * - Faturamento anual contribui ate 700 pontos (faturamento / 10000, limitado a 700)
 * - Tempo de mercado contribui ate 300 pontos (anos * 30, limitado a 300)
 */

@Component
public class RegraScorePJ implements RegraScore {

    private static final double PESO_FATURAMENTO = 10_000.0;
    private static final int TETO_FATURAMENTO = 700;
    private static final int PESO_TEMPO_MERCADO = 30;
    private static final int TETO_TEMPO_MERCADO = 300;

    @Override
    public TipoCliente getTipoSuportado() {
        return TipoCliente.PJ;
    }

    @Override
    public int calcularScore(ScoreRequestDTO request) {

        double pontosFaturamento = Math.min(request.getFaturamentoAnual() / PESO_FATURAMENTO, TETO_FATURAMENTO);
        double pontosTempoMercado = Math.min(request.getTempoMercadoAnos() * PESO_TEMPO_MERCADO, TETO_TEMPO_MERCADO);

        int scoreFinal = (int) Math.round(pontosFaturamento + pontosTempoMercado);
        return Math.min(scoreFinal, 1000);
    }
}
