package br.gov.ce.arce.spgc.model.enumeration;

public enum SolicitacaoStatus {

    AGUARDANDO_ANALISE("Aguardando Análise"),
    EM_ANALISE("Em Análise"),
    EM_ANALISE_ASSESSORIA("Em Análise (Assessoria)"),
    DOCUMENTACAO_PENDENTE("Documentação Pendente"),
    CONCLUIDO("Concluído"),
    REJEITADO("Rejeitado");

    private final String descricao;

    SolicitacaoStatus(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    /**
     * Retorna true se o status for considerado "em aberto"
     */
    public boolean isEmAberto() {
        return this == AGUARDANDO_ANALISE
                || this == EM_ANALISE
                || this == EM_ANALISE_ASSESSORIA
                || this == DOCUMENTACAO_PENDENTE;
    }
}