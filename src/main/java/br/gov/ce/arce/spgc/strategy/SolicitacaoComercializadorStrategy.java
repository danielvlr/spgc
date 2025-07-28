package br.gov.ce.arce.spgc.strategy;


import br.gov.ce.arce.spgc.exception.BusinessException;
import br.gov.ce.arce.spgc.model.dto.CreateSolicitacaoRequest;
import br.gov.ce.arce.spgc.model.entity.Solicitacao;
import br.gov.ce.arce.spgc.model.enumeration.SolicitacaoStatus;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Component("COMERCIALIZADOR")
public class SolicitacaoComercializadorStrategy implements SolicitacaoStrategy {

    public SolicitacaoStatus analistaFinalizaSolicitacao(){
        return SolicitacaoStatus.EM_ANALISE_ASSESSORIA;
    }

    public void valida(Solicitacao solicitacao) {
        var regras = List.of(
                new Regra("I", List.of(1, 2, 3), RegraTipo.PELO_MENOS_UM,
                        "Ato constitutivo, estatuto ou contrato social em vigor, devidamente registrado, e, no caso de sociedade por ações, acompanhado de documentos de eleição de seus administradores."),
                new Regra("II", List.of(4), RegraTipo.TODOS,
                        "Prova de inscrição no Cadastro Nacional da Pessoa Jurídica - CNPJ."),
                new Regra("III", List.of(5, 6, 7), RegraTipo.TODOS,
                        "Prova de regularidade para com a Fazenda Federal, Estadual e Municipal do domicílio ou sede da pessoa jurídica."),
                new Regra("IV", List.of(8, 9), RegraTipo.TODOS,
                        "Prova de regularidade relativa à Seguridade Social e ao FGTS."),
                new Regra("V", List.of(10, 11), RegraTipo.TODOS,
                        "Balanço patrimonial e demonstrações contábeis do último exercício social."),
                new Regra("VI", List.of(12), RegraTipo.TODOS,
                        "Certidão negativa de falência ou recuperação judicial."),
                new Regra("VII", List.of(13, 14), RegraTipo.PELO_MENOS_UM,
                        "Prova de capital mínimo integralizado ou de patrimônio líquido mínimo de R$ 1.000.000,00."),
                new Regra("VIII", List.of(15), RegraTipo.TODOS,
                        "Relação da equipe técnica envolvida na atividade de comercialização."),
                new Regra("IX", List.of(16), RegraTipo.TODOS,
                        "Provas de que dispõe dos volumes de gás para comercialização em áreas de concessão."),
                new Regra("X", List.of(17), RegraTipo.TODOS,
                        "Termo de Compromisso para Fins de Autorização de Comercializador."),
                new Regra("XI", List.of(18), RegraTipo.TODOS,
                        "Autorização de comercializador de gás natural emitida pela ANP."),
                new Regra("XII", List.of(19), RegraTipo.TODOS,
                        "Requerimento para Autorização do Exercício de Atividade no Mercado Livre de Gás no Estado do Ceará.")
        );

        validaRegras(solicitacao, regras);
    }
}
