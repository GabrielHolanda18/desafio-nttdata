package com.desafio.nttdata.servicoemprestimo.controller;

import com.desafio.nttdata.servicoemprestimo.dto.EmprestimoRequestDTO;
import com.desafio.nttdata.servicoemprestimo.dto.EmprestimoResponseDTO;
import com.desafio.nttdata.servicoemprestimo.facade.EmprestimoFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/emprestimos")
@Tag(name = "Emprestimos", description = "Avaliação e aprovação de pedidos de empréstimo (PF e PJ)")
public class EmprestimoController {


    private final EmprestimoFacade emprestimoFacade;

    public EmprestimoController(EmprestimoFacade emprestimoFacade) {
        this.emprestimoFacade = emprestimoFacade;
    }

    @PostMapping
    @Operation(summary = "Avalia um pedido de emprestimo (valida, consulta score e aplica regra de aprovacao)")
    public ResponseEntity<EmprestimoResponseDTO> solicitar(@Valid @RequestBody EmprestimoRequestDTO request) {
        EmprestimoResponseDTO response = emprestimoFacade.processar(request);
        return ResponseEntity.ok(response);
    }
}
