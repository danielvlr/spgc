package br.gov.ce.arce.spgc.scheduler;

import br.gov.ce.arce.spgc.model.entity.Solicitacao;
import br.gov.ce.arce.spgc.model.enumeration.SolicitacaoStatus;
import br.gov.ce.arce.spgc.service.SolicitacaoService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
@Slf4j
public class Scheduler {

    private final SolicitacaoService service;

    @Transactional
    @Scheduled(cron = "0 1 * * * *") // Executa diariamente às 2h da manhã
    public void atualizaSolicitacoesDocumentacaoPendente() {
        log.info("Job started - atualizaSolicitacoesDocumentacaoPendente");
        service.atualizaSolicitacoesDocumentacaoPendente();
    }

}
