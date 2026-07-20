package com.desafio.nttdata.servicoemprestimo.cliente;

import com.desafio.nttdata.servicoemprestimo.dto.ScoreRequestDTO;
import com.desafio.nttdata.servicoemprestimo.dto.ScoreResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/*
    Usando o Feign
 */
@FeignClient(name = "servico-score", url = "${score.service.url}")
public interface ScoreCliente {

    @PostMapping("/score")
    ScoreResponseDTO calcularScore(@RequestBody ScoreRequestDTO request);
}