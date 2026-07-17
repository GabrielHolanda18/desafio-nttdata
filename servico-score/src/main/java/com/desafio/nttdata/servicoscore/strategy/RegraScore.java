package com.desafio.nttdata.servicoscore.strategy;

import com.desafio.nttdata.servicoscore.dto.ScoreRequestDTO;
import com.desafio.nttdata.servicoscore.dto.TipoCliente;

/*
    * ‘Interface’ cada tipo de cliente (PF, PJ) implementa a sua propria regra de cálculo de score.

    * Caso seja necessario suportar um novo tipo de cliente no futuro (ex: MEI),
    * basta criar uma nova classe implementando essa ‘interface’ - sem alterar
    * as regras existentes, que já estão testadas e em uso.
 */

public interface RegraScore {

    TipoCliente getTipoSuportado();

    int calcularScore(ScoreRequestDTO requestDTO);
}
