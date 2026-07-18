package com.desafio.nttdata.servicoemprestimo.validacao;

import com.desafio.nttdata.servicoemprestimo.dto.EmprestimoRequestDTO;
import com.desafio.nttdata.servicoemprestimo.exception.ValidacaoException;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(3)
public class ValidadorValorSolicitado extends ValidadorBase {

    private static final double VALOR_MAXIMO_PERMITIDO = 500_000.0;

    @Override
    protected void doValidar(EmprestimoRequestDTO request) {
        if (request.getValorSolicitado() > VALOR_MAXIMO_PERMITIDO) {
            throw new ValidacaoException(
                    "Valor solicitado excede o limite maximo de R$ " + VALOR_MAXIMO_PERMITIDO);
        }
    }
}
