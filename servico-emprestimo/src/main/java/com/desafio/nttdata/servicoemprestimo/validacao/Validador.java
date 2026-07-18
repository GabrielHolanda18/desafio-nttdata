package com.desafio.nttdata.servicoemprestimo.validacao;

import com.desafio.nttdata.servicoemprestimo.dto.EmprestimoRequestDTO;

public interface Validador {

    Validador setProximo(Validador proximo);

    void validar(EmprestimoRequestDTO emprestimo);
}
