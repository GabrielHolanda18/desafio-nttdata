package com.desafio.nttdata.servicoscore.service;

import com.desafio.nttdata.servicoscore.dto.ScoreResponseDTO;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ScoreService {

    private final Map<TipoCliente, RegraScore> regras;

    public ScoreService(List<RegraScore> regrasDisponiveis) {
        this.regras = regrasDisponiveis.stream()
                .collect(Collectors.toMap(RegraScore::getTipoSuportado, Function.identity()));
    }

}
