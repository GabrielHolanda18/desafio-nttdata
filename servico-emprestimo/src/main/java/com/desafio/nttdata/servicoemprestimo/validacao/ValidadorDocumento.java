package com.desafio.nttdata.servicoemprestimo.validacao;

import com.desafio.nttdata.servicoemprestimo.dto.EmprestimoRequestDTO;
import com.desafio.nttdata.servicoemprestimo.exception.ValidacaoException;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class ValidadorDocumento extends ValidadorBase {

    @Override
    protected void doValidar(EmprestimoRequestDTO request) {
        if (request.getDocumento() == null || request.getDocumento().isBlank()) {
            throw new ValidacaoException("O documento (CPF ou CNPJ) e obrigatorio");
        }
    }
}
