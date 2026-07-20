package com.desafio.nttdata.servicoemprestimo.validacao;

import com.desafio.nttdata.servicoemprestimo.dto.EmprestimoRequestDTO;

public abstract class ValidadorBase implements Validador {

    private Validador proximo;

    @Override
    public Validador setProximo(Validador proximo) {
        this.proximo = proximo;
        return proximo; // permite encadear: v1.setProximo(v2).setProximo(v3)...
    }

    @Override
    public final void validar(EmprestimoRequestDTO request) {
        doValidar(request); // executa a validação dele mesmo
        if (proximo != null) {
            proximo.validar(request); // chama o próximo, que faz o mesmo
        }
    }

    /*
     * Cada validacao concreta implementa apenas a sua propria regra aqui.
     * O repasse para o proximo elo fica resolvido na classe base.
     */
    protected abstract void doValidar(EmprestimoRequestDTO request);
}
