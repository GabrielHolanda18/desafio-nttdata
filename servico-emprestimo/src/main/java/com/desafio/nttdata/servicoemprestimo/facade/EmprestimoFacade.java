package com.desafio.nttdata.servicoemprestimo.facade;

import com.desafio.nttdata.servicoemprestimo.cliente.ScoreCliente;
import com.desafio.nttdata.servicoemprestimo.config.CadeiaValidacaoConfig;
import com.desafio.nttdata.servicoemprestimo.dto.*;
import com.desafio.nttdata.servicoemprestimo.entity.EmprestimoEntity;
import com.desafio.nttdata.servicoemprestimo.repository.EmprestimoRepository;
import com.desafio.nttdata.servicoemprestimo.strategy.RegraAprovacao;
import com.desafio.nttdata.servicoemprestimo.strategy.ResultadoAprovacao;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class EmprestimoFacade {

    private final CadeiaValidacaoConfig cadeiaValidacao;
    private final ScoreCliente scoreClient;
    private final EmprestimoRepository emprestimoRepository;
    private final Map<TipoCliente, RegraAprovacao> regrasAprovacao;

    public EmprestimoFacade(CadeiaValidacaoConfig cadeiaValidacao,
                            ScoreCliente scoreClient,
                            EmprestimoRepository emprestimoRepository,
                            List<RegraAprovacao> regrasDisponiveis) {
        this.cadeiaValidacao = cadeiaValidacao;
        this.scoreClient = scoreClient;
        this.emprestimoRepository = emprestimoRepository;
        this.regrasAprovacao = regrasDisponiveis.stream()
                .collect(Collectors.toMap(RegraAprovacao::getTipoSuportado, Function.identity()));
    }

    public EmprestimoResponseDTO processar(EmprestimoRequestDTO request) {

        // documento -> dados por tipo -> valor
        cadeiaValidacao.getPrimeiroValidador().validar(request);

        // OpenFeign: chamada sincrona ao servico-score
        ScoreRequestDTO scoreRequest = new ScoreRequestDTO(
                request.getTipoCliente(), request.getDocumento(), request.getIdade(),
                request.getRendaMensal(), request.getFaturamentoAnual(), request.getTempoMercadoAnos());
        ScoreResponseDTO scoreResponse = scoreClient.calcularScore(scoreRequest);

        // aplica a regra de aprovacao de acordo com o tipo de cliente
        RegraAprovacao regra = regrasAprovacao.get(request.getTipoCliente());
        ResultadoAprovacao resultado = regra.avaliar(request, scoreResponse);

        // Persiste o resultado no banco (Repository Pattern)
        EmprestimoEntity entity = new EmprestimoEntity(
                request.getDocumento(), request.getTipoCliente(), request.getValorSolicitado(),
                scoreResponse.getScore(), scoreResponse.getClassificacao(),
                resultado.aprovado(), resultado.motivo(), resultado.valorAprovado());
        emprestimoRepository.save(entity);

        // Montar a resposta final para o Controller
        return new EmprestimoResponseDTO(
                request.getDocumento(),
                request.getTipoCliente(),
                request.getValorSolicitado(),
                scoreResponse.getScore(),
                scoreResponse.getClassificacao(),
                resultado.aprovado(),
                resultado.motivo(),
                resultado.valorAprovado()
        );
    }
}