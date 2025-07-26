package br.gov.ce.arce.spgc.strategy;


import br.gov.ce.arce.spgc.model.dto.CreateSolicitacaoRequest;
import br.gov.ce.arce.spgc.model.entity.Solicitacao;
import br.gov.ce.arce.spgc.model.enumeration.SolicitacaoStatus;
import br.gov.ce.arce.spgc.model.mapper.SolicitacaoMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component("AUTOPRODUTOR")
@AllArgsConstructor
public class SolicitacaoAutoprodutorStrategy implements SolicitacaoStrategy {

    private final SolicitacaoMapper mapper;

    public SolicitacaoStatus analistaFinalizaSolicitacao(){
        return SolicitacaoStatus.EM_ANALISE_ASSESSORIA;
    }

    public void valida(Solicitacao solicitacao){
        var regras = List.of(
                new Regra("I", List.of(1, 2, 3), RegraTipo.PELO_MENOS_UM,
                        "Ato constitutivo, estatuto ou contrato social em vigor, devidamente registrado."),
                new Regra("II", List.of(4), RegraTipo.TODOS,
                        "Prova de inscrição no Cadastro Nacional da Pessoa Jurídica - CNPJ."),
                new Regra("III", List.of(5, 6, 7), RegraTipo.TODOS,
                        "Prova de regularidade para com a Fazenda Federal, Estadual e Municipal."),
                new Regra("IV", List.of(20, 21), RegraTipo.PELO_MENOS_UM,
                        "Registro emitido pela ANP enquadrando-o como Autoimportador ou Autoprodutor."),
                new Regra("V", List.of(22, 23), RegraTipo.PELO_MENOS_UM,
                        "Ato comprobatório do concessionário de possibilidade técnica ou acordo técnico e comercial para nova canalização."),
                new Regra("VI", List.of(24), RegraTipo.TODOS,
                        "Garantias de volumes de gás para entrega ao concessionário."),
                new Regra("VII", List.of(19), RegraTipo.TODOS,
                        "Requerimento para Autorização do Exercício de Atividade no Mercado Livre de Gás no Estado do Ceará.")
        );
        validaRegras(solicitacao, regras);
    }
}