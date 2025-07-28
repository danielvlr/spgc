package br.gov.ce.arce.spgc.model.enumeration;

import java.util.List;

public enum SolicitacaoStatus {

    AGUARDANDO_ANALISE("Aguardando Análise"),
    EM_ANALISE("Em Análise"),
    EM_ANALISE_ASSESSORIA("Em Análise (Assessoria)"),
    DOCUMENTACAO_PENDENTE("Documentação Pendente"),
    AUTORIZADO("Autorizado"),
    NAO_AUTORIZADO("Não Autorizado");

    private final String descricao;

    SolicitacaoStatus(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public static List<SolicitacaoStatus> emAberto() {
        return List.of(AGUARDANDO_ANALISE, EM_ANALISE, EM_ANALISE_ASSESSORIA, DOCUMENTACAO_PENDENTE);
    }
}