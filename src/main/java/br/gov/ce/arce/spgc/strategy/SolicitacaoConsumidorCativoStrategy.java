package br.gov.ce.arce.spgc.strategy;


import br.gov.ce.arce.spgc.model.dto.CreateSolicitacaoRequest;
import br.gov.ce.arce.spgc.model.entity.Solicitacao;
import br.gov.ce.arce.spgc.model.enumeration.SolicitacaoStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component("CONSUMIDOR_CATIVO")
public class SolicitacaoConsumidorCativoStrategy implements SolicitacaoStrategy {

    public SolicitacaoStatus analistaFinalizaSolicitacao(){
        return SolicitacaoStatus.CONCLUIDO;
    }

    public void valida(Solicitacao solicitacao) {
        var regras = List.of(
                new Regra("I", List.of(1, 2, 3), RegraTipo.PELO_MENOS_UM,
                        "Ato constitutivo, estatuto ou contrato social em vigor."),
                new Regra("II", List.of(4), RegraTipo.TODOS,
                        "Prova de inscrição no Cadastro Nacional da Pessoa Jurídica - CNPJ."),
                new Regra("III", List.of(5, 6, 7), RegraTipo.TODOS,
                        "Prova de regularidade para com a Fazenda Federal, Estadual e Municipal."),
                new Regra("IV", List.of(30), RegraTipo.TODOS,
                        "Contrato de fornecimento de gás firmado com a concessionária."),
                new Regra("V", List.of(31, 32), RegraTipo.PELO_MENOS_UM,
                        "Rescisão ou revisão do contrato com a concessionária."),
                new Regra("VI", List.of(25, 26), RegraTipo.PELO_MENOS_UM,
                        "Contrato de comercialização ou termo de compromisso de aquisição de gás."),
                new Regra("VII", List.of(27, 28), RegraTipo.PELO_MENOS_UM,
                        "Contrato de uso do sistema de distribuição ou termo de compromisso."),
                new Regra("VIII", List.of(29), RegraTipo.TODOS,
                        "Acordo operacional para o mercado livre."),
                new Regra("IX", List.of(19), RegraTipo.TODOS,
                        "Requerimento para Autorização do Exercício de Atividade no Mercado Livre de Gás no Estado do Ceará.")
        );

        validaRegras(solicitacao, regras);
    }
}