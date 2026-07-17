package com.desafio.nttdata.servicoscore.strategy;

import com.desafio.nttdata.servicoscore.dto.ScoreRequestDTO;
import com.desafio.nttdata.servicoscore.dto.TipoCliente;

public interface RegraScore {

    TipoCliente getTipoSuportado();

    int calcularScore(ScoreRequestDTO requestDTO);
}
