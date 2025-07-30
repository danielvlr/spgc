package br.gov.ce.arce.spgc.service;

import br.gov.ce.arce.spgc.client.email.EmailClient;
import br.gov.ce.arce.spgc.component.EmailProperties;
import br.gov.ce.arce.spgc.model.entity.Arquivo;
import br.gov.ce.arce.spgc.model.entity.Solicitacao;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class EmailSpgcService {

    public final EmailClient emailService;
    public final TemplateEngine templateEngine;
    public final EmailProperties emailProperties;

    public void enviaEmailSolicitante(Arquivo arquivo){
        var solicitacao = arquivo.getSolicitacao();
        var to = solicitacao.getEmail();
        var from = emailProperties.getFrom();

        String subject = "Solicitação mudou de status";
        Map<String, Object> model = new HashMap<>();
        model.put("numeroSolicitacao", solicitacao.getId());
        model.put("empresa", solicitacao.getNomeEmpresa());
        model.put("email", solicitacao.getEmail());
        model.put("urlArquivo", arquivo.getUrl());
        model.put("tipoDocumento", arquivo.getTipoDocumento());
        model.put("status", "Documento rejeitado");
        model.put("justificativa", arquivo.getJustificativa());
        model.put("urlCentralServico", emailProperties.getUrl());

        Context context = new Context();
        context.setVariable("data", model);
        context.setVariable("fragment", "fragments/solicitacao-arquivo");
        String content = templateEngine.process("email-template", context);
        emailService.sendEmail(to, from, subject, content);
    }

    public void enviaEmailConfirmacaoSolicitante(Solicitacao solicitacao) {
        var to = solicitacao.getEmail();
        var from = emailProperties.getFrom();

        String subject = "Solicitação recebida";
        Map<String, Object> model = new HashMap<>();
        model.put("numeroSolicitacao", solicitacao.getId());
        model.put("token", solicitacao.getToken());
        model.put("urlCentralServico", emailProperties.getUrl());

        Context context = new Context();
        context.setVariable("data", model);
        context.setVariable("fragment", "fragments/solicitacao-confirmacao");
        String content = templateEngine.process("email-template", context);
        emailService.sendEmail(to, from, subject, content);
    }

    public void enviaEmailConfirmacaoCentral(Solicitacao solicitacao) {
        var to = emailProperties.getFrom();
        var from = emailProperties.getFrom();
        String subject = "Nova solicitação recebida";

        Map<String, Object> model = new HashMap<>();
        model.put("numeroSolicitacao", solicitacao.getId());
        model.put("token", solicitacao.getToken());
        model.put("urlCentralServico", emailProperties.getUrl());

        Context context = new Context();
        context.setVariable("data", model);
        context.setVariable("fragment", "fragments/solicitacao-confirmacao");
        String content = templateEngine.process("email-template", context);
        emailService.sendEmail(to, from, subject, content);
    }

    public void enviaEmailPendenciaDocumentoSolicitante(Solicitacao solicitacao) {
        var to = solicitacao.getEmail();
        var from = emailProperties.getFrom();

        String subject = "Solicitação recebida";
        Map<String, Object> model = new HashMap<>();
        model.put("numeroSolicitacao", solicitacao.getId());
        model.put("token", solicitacao.getToken());
        model.put("urlCentralServico", emailProperties.getUrl());

        Context context = new Context();
        context.setVariable("data", model);
        context.setVariable("fragment", "fragments/solicitacao-pendencia-documento");
        String content = templateEngine.process("email-template", context);
        emailService.sendEmail(to, from, subject, content);
    }

    public void enviaEmailSolicitacaoConcluida(Solicitacao solicitacao) {
        var to = solicitacao.getEmail();
        var from = emailProperties.getFrom();

        String subject = "Solicitação recebida";
        Map<String, Object> model = new HashMap<>();
        model.put("numeroSolicitacao", solicitacao.getId());
        model.put("token", solicitacao.getToken());
        model.put("urlCentralServico", emailProperties.getUrl());

        Context context = new Context();
        context.setVariable("data", model);
        context.setVariable("fragment", "fragments/solicitacao-concluida");
        String content = templateEngine.process("email-template", context);
        emailService.sendEmail(to, from, subject, content);
    }

    public void enviaEmailSolicitacaoRejeitada(Solicitacao solicitacao) {
        var to = solicitacao.getEmail();
        var from = emailProperties.getFrom();

        String subject = "Solicitação recebida";
        Map<String, Object> model = new HashMap<>();
        model.put("numeroSolicitacao", solicitacao.getId());
        model.put("token", solicitacao.getToken());
        model.put("urlCentralServico", emailProperties.getUrl());

        Context context = new Context();
        context.setVariable("data", model);
        context.setVariable("fragment", "fragments/solicitacao-rejeitada");
        String content = templateEngine.process("email-template", context);
        emailService.sendEmail(to, from, subject, content);
    }

    public void enviaEmailConselhoDiretor(Solicitacao solicitacao) {
        var to = emailProperties.getFrom();
        var from = emailProperties.getFrom();
        String subject = "Nova solicitação recebida";

        Map<String, Object> model = new HashMap<>();
        model.put("numeroSolicitacao", solicitacao.getId());
        model.put("token", solicitacao.getToken());
        model.put("urlCentralServico", emailProperties.getUrl());

        Context context = new Context();
        context.setVariable("data", model);
        context.setVariable("fragment", "fragments/solicitacao-conselho-diretor");
        String content = templateEngine.process("email-template", context);
        emailService.sendEmail(to, from, subject, content);
    }

    public void enviaEmailEmAnaliseSolicitante(Solicitacao solicitacao) {
        var to = emailProperties.getFrom();
        var from = emailProperties.getFrom();
        String subject = "Nova solicitação recebida";

        Map<String, Object> model = new HashMap<>();
        model.put("numeroSolicitacao", solicitacao.getId());
        model.put("token", solicitacao.getToken());
        model.put("urlCentralServico", emailProperties.getUrl());

        Context context = new Context();
        context.setVariable("data", model);
        context.setVariable("fragment", "fragments/solicitacao-conselho-diretor");
        String content = templateEngine.process("email-template", context);
        emailService.sendEmail(to, from, subject, content);
    }
}