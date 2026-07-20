package com.desafio.nttdata.servicoemprestimo.config;

import com.desafio.nttdata.servicoemprestimo.validacao.Validador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CadeiaValidacaoConfig {

    private final Validador primeiroValidador;

    /*
    Pega os validadores e monta uma fila conectada
     */
    @Autowired
    public CadeiaValidacaoConfig(List<Validador> validadoresOrdenados) {
        for (int i = 0; i < validadoresOrdenados.size() - 1; i++) {
            validadoresOrdenados.get(i).setProximo(validadoresOrdenados.get(i + 1));
        }
        this.primeiroValidador = validadoresOrdenados.isEmpty() ? null : validadoresOrdenados.get(0);
    }

    public Validador getPrimeiroValidador() {
        return primeiroValidador;
    }
}
