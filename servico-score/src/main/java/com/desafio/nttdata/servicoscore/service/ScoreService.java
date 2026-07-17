package com.desafio.nttdata.servicoscore.service;

import com.desafio.nttdata.servicoscore.dto.ScoreRequestDTO;
import com.desafio.nttdata.servicoscore.dto.ScoreResponseDTO;
import com.desafio.nttdata.servicoscore.dto.TipoCliente;
import com.desafio.nttdata.servicoscore.strategy.RegraScore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ScoreService {

    private final Map<TipoCliente, RegraScore> regras;

    public ScoreService(List<RegraScore> regrasDisponiveis) {
        this.regras = regrasDisponiveis.stream()
                .collect(Collectors.toMap(RegraScore::getTipoSuportado, Function.identity()));
    }

    public ScoreResponseDTO calcularScore(ScoreRequestDTO request) {
        RegraScore regra = regras.get(request.getTipoCliente());

        if (regra == null) {
            throw new IllegalArgumentException("Regra inexistente para esse tipo: " + request.getTipoCliente());
        }
        int score = regra.calcularScore(request);

        String classificacao = classificar(score);

        return new ScoreResponseDTO(request.getDocumento(), request.getTipoCliente(), score, classificacao);
    }

    private String classificar(int score) {
        if (score >= 700) return "ALTO";
        if (score >= 400) return "MEDIO";
        return "BAIXO";
    }

}
