package com.desafio.nttdata.servicoscore.controller;

import com.desafio.nttdata.servicoscore.dto.ScoreRequestDTO;
import com.desafio.nttdata.servicoscore.dto.ScoreResponseDTO;
import com.desafio.nttdata.servicoscore.service.ScoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/score")
@Tag(name = "Score de Credito", description = "Calculo de score para clientes PF e PJ")
public class ScoreController {

    private final ScoreService scoreService;

    public ScoreController(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    @PostMapping
    @Operation(summary = "Calcula o score de credito de um cliente PF ou PJ")
    public ResponseEntity<ScoreResponseDTO> calcularScore(@Valid @RequestBody ScoreRequestDTO request) {
        ScoreResponseDTO  response = scoreService.calcularScore(request);
        return ResponseEntity.ok(response);
    }

}
