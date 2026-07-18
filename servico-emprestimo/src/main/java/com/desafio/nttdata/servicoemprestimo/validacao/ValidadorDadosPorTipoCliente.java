package com.desafio.nttdata.servicoemprestimo.validacao;

import com.desafio.nttdata.servicoemprestimo.dto.EmprestimoRequestDTO;
import com.desafio.nttdata.servicoemprestimo.exception.ValidacaoException;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class ValidadorDadosPorTipoCliente extends ValidadorBase {

    @Override
    protected void doValidar(EmprestimoRequestDTO request) {
        switch (request.getTipoCliente()) {
            case PF -> {
                if (request.getIdade() == null || request.getRendaMensal() == null) {
                    throw new ValidacaoException("Para cliente PF, idade e rendaMensal sao obrigatorios");
                }
                if (request.getIdade() < 18) {
                    throw new ValidacaoException("Cliente PF deve ser maior de idade para solicitar emprestimo");
                }
            }
            case PJ -> {
                if (request.getFaturamentoAnual() == null || request.getTempoMercadoAnos() == null) {
                    throw new ValidacaoException("Para cliente PJ, faturamentoAnual e tempoMercadoAnos sao obrigatorios");
                }
            }
        }
    }
}
