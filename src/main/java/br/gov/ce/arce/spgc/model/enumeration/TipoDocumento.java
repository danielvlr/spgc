package br.gov.ce.arce.spgc.model.enumeration;

public enum TipoDocumento {

    ATO_CONSTITUTIVO(1, "Ato Constitutivo"),
    ESTATUTO(2, "Estatuto"),
    CONTRATO_SOCIAL(3, "Contrato Social"),
    CARTAO_CNPJ(4, "Cartão CNPJ"),
    CND_FAZENDA_FEDERAL(5, "Certidão Negativa de Débitos (Fazenda Federal)"),
    CND_FAZENDA_ESTADUAL(6, "Certidão Negativa de Débitos (Fazenda Estadual)"),
    CND_FAZENDA_MUNICIPAL(7, "Certidão Negativa de Débitos (Fazenda Municipal)"),
    CND_INSS(8, "Certidão Negativa de Débitos (INSS)"),
    CND_FGTS(9, "Certidão Negativa de Débitos (FGTS)"),
    BALANCO_PATRIMONIAL(10, "Balanço Patrimonial"),
    DEMONSTRACOES_CONTABEIS(11, "Demonstrações Contábeis"),
    CND_FALENCIA(12, "Certidão Negativa de Falência ou Concordata"),
    CAPITAL_INTEGRALIZADO(13, "Capital integralizado"),
    PATRIMONIO_LIQUIDO(14, "Patrimônio Líquido"),
    EQUIPE_TECNICA(15, "Equipe Técnica"),
    VOLUME_GAS_CONCESSAO(16, "Volume de gás em áreas de concessão"),
    TERMO_COMPROMISSO_AUTORIZACAO_COMERCIALIZADOR(17, "Termo de Compromisso para Fins de Autorização de Comercializador"),
    AUTORIZACAO_COMERCIALIZADOR_ANP(18, "Autorização de comercializador de gás natural emitida pela ANP"),
    REQUERIMENTO_MERCADO_LIVRE_GAS(19, "Requerimento para Exercício de Atividade no Mercado Livre de Gás."),
    REGISTRO_AUTOIMPORTADOR_ANP(20, "Registro de Autoimportador emitido pela ANP"),
    REGISTRO_AUTOPRODUTOR_ANP(21, "Registro de Autoprodutor emitido pela ANP"),
    ATO_COMPROBATORIO_ACESSO_SISTEMA(22, "Ato comprobatório de acesso ao sistema de distribuição em operação do concessionário"),
    ACORDO_TECNICO_NOVA_CANALIZACAO(23, "Acordo técnico ou comercial para nova canalização"),
    GARANTIA_VOLUME_GAS_RECEPCAO(24, "Garantias de volume de gás nos pontos de recepção"),
    CONTRATO_COMERCIALIZACAO(25, "Contrato de comercialização com o agente comercializador"),
    TERMO_COMPROMISSO_AQUISICAO_GAS(26, "Termo de compromisso de aquisição de gás com o agente comercializador"),
    CONTRATO_USO_SISTEMA_DISTRIBUICAO(27, "Contrato de uso do sistema de distribuição de gás"),
    TERMO_COMPROMISSO_USO_SISTEMA_DISTRIBUICAO(28, "Termo de compromisso para uso do sistema de distribuição de gás"),
    ACORDO_OPERACIONAL_MERCADO_LIVRE(29, "Acordo operacional para o mercado livre"),
    CONTRATO_FORNECIMENTO_CONCESSIONARIA(30, "Contrato de fornecimento de gás firmado com a concessionária"),
    RESCISAO_CONTRATO_CONCESSIONARIA(31, "Rescisão do contrato com a concessionária"),
    REVISAO_CONTRATO_CONCESSIONARIA(32, "Revisão do contrato com a concessionária");

    private final int codigo;
    private final String descricao;

    TipoDocumento(int codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public static TipoDocumento fromCodigo(int codigo) {
        for (TipoDocumento doc : values()) {
            if (doc.codigo == codigo) {
                return doc;
            }
        }
        throw new IllegalArgumentException("Código inválido: " + codigo);
    }
}
