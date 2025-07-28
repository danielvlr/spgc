package br.gov.ce.arce.spgc.service;

import br.gov.ce.arce.spgc.client.minio.MinioService;
import br.gov.ce.arce.spgc.exception.BusinessException;
import br.gov.ce.arce.spgc.model.BasePageResponse;
import br.gov.ce.arce.spgc.model.dto.*;
import br.gov.ce.arce.spgc.model.entity.Justificativa;
import br.gov.ce.arce.spgc.model.entity.Solicitacao;
import br.gov.ce.arce.spgc.model.enumeration.SolicitacaoStatus;
import br.gov.ce.arce.spgc.model.mapper.JustificativaMapper;
import br.gov.ce.arce.spgc.model.mapper.SolicitacaoMapper;
import br.gov.ce.arce.spgc.repository.JustificativaRepository;
import br.gov.ce.arce.spgc.repository.SolicitacaoRepository;
import br.gov.ce.arce.spgc.strategy.SolicitacaoStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class SolicitacaoService {

    private final MinioService minioService;
    private final SolicitacaoRepository repository;
    private final SolicitacaoMapper mapper;
    private final EmailSpgcService emailSpgcService;
    private final Map<String, SolicitacaoStrategy> strategyMap;
    private final JustificativaMapper justificativaMapper;
    private final JustificativaRepository justificativaRepository;

    public SolicitacaoResponse criarSolicitacao(CreateSolicitacaoRequest request) {
        SolicitacaoStrategy strategy = getStrategy(request.tipoSolicitacao());

        // converte o request em entity
        var solicitacao = mapper.toEntity(request);

        // Valida regra geral
        validaCriacao(solicitacao);

        // Valida por tipo de solicitacao
        strategy.valida(solicitacao);

        // Salvando solicitacao para gerar os Id na base de dados
        solicitacao = repository.save(solicitacao);

        // Envia arquivo para o minio
        solicitacao.getArquivos().stream().forEach(arquivo -> {
            var url = minioService.saveFileBase64(arquivo.getConteudoBase64(), "teste", arquivo.getTipoDocumento().name(), arquivo.getId());
            arquivo.setUrl(url);
        });

        // Cria token unico para solicitacao


        // Envia email solicitante confirmando o recebimento da solicitacao
        emailSpgcService.enviaEmailConfirmacaoSolicitante(solicitacao);

        // Envia email central confirmando o recebimento da solicitacao
        emailSpgcService.enviaEmailConfirmacaoCentral(solicitacao);

        return mapper.toSolicitacaoResponse(repository.save(solicitacao));
    }

    private SolicitacaoStrategy getStrategy(String tipoSolicitacao) {
        SolicitacaoStrategy strategy = strategyMap.get(tipoSolicitacao);
        if (strategy == null) {
            throw BusinessException.createNotFoundBusinessException("Tipo de solicição não suportado: " + tipoSolicitacao);
        }
        return strategy;
    }

    public BasePageResponse<SolicitacaoResponse> findAllByFilters(SolicitacaoRequest payload, PageRequest pageable) {
        var solicitacao = mapper.toEntity(payload);
        var result = repository.findAll(Example.of(solicitacao), pageable).map(mapper::toSolicitacaoResponse);
        return BasePageResponse.createBySpringPage(result);
    }

    public SolicitacaoResponse findById(Long id) {
        var result = repository.getReferenceById(id);
        return mapper.toSolicitacaoResponse(result);
    }

    public List<SolicitacaoResponse> findByCnpj(String cnpj) {
        cnpj = cnpj.replaceAll("\\D", "");
        var result = repository.findByCnpj(cnpj);
        return mapper.toSolicitacaoResponseList(result);
    }

    public boolean validaTodosDocumentosAvaliadosOuPendenteAnalista(Solicitacao solicitacao) {
        return solicitacao.getArquivos().stream()
                .allMatch(a -> Objects.isNull(a) || a.getAprovado() );
    }

    public boolean validaDocumentos(Solicitacao solicitacao, Predicate<Boolean> predicate) {
        return solicitacao.getArquivos().stream()
                .allMatch(a -> predicate.test(a.getAprovado()));
    }

    public boolean validaTodosDocumentosAvaliados(Solicitacao solicitacao) {
        return validaDocumentos(solicitacao, Objects::nonNull);
    }

    public boolean validaTodosDocumentosConfirmados(Solicitacao solicitacao) {
        return validaDocumentos(solicitacao, Boolean.TRUE::equals);
    }

    public void todosDocumentosAnalisadosSolicitacao(Solicitacao solicitacao) {
        var strategy = getStrategy(solicitacao.getTipoSolicitacao().name());

        if (validaTodosDocumentosAvaliados(solicitacao)) {
            if (validaTodosDocumentosConfirmados(solicitacao)) {
                solicitacao.setStatus(strategy.analistaFinalizaSolicitacao());
            } else {
                solicitacao.setStatus(SolicitacaoStatus.DOCUMENTACAO_PENDENTE);
            }
        }

        var statusPendentes = List.of(SolicitacaoStatus.AUTORIZADO, SolicitacaoStatus.EM_ANALISE_ASSESSORIA);
        if (statusPendentes.contains(solicitacao.getStatus())) {
            repository.save(solicitacao);
            if (solicitacao.getStatus() == SolicitacaoStatus.AUTORIZADO) {
                emailSpgcService.enviaEmailSolicitacaoConcluida(solicitacao);
            }
            if (solicitacao.getStatus() == SolicitacaoStatus.EM_ANALISE_ASSESSORIA) {
                emailSpgcService.enviaEmailConselhoDiretor(solicitacao);
            }
        }
    }

    public SolicitacaoResponse analistaNaoAutorizaSolicitacao(Long id, JustificativaRequest payload) {
        var solicitacao = repository.getReferenceById(id);
        var justificativa = justificativaMapper.toEntity(payload);
        solicitacao.setJustificativa(justificativa);
        solicitacao.setStatus(SolicitacaoStatus.NAO_AUTORIZADO);
        emailSpgcService.enviaEmailSolicitacaoRejeitada(solicitacao);
        return mapper.toSolicitacaoResponse(repository.save(solicitacao));
    }

    public SolicitacaoResponse conselhorDiretorNaoAutorizaSolicitacao(Long id, JustificativaRequest payload) {
        var solicitacao = repository.getReferenceById(id);
        var justificativa = justificativaMapper.toEntity(payload);
        solicitacao.setJustificativa(justificativa);
        solicitacao.setStatus(SolicitacaoStatus.NAO_AUTORIZADO);
        emailSpgcService.enviaEmailSolicitacaoRejeitada(solicitacao);
        return mapper.toSolicitacaoResponse(repository.save(solicitacao));
    }

    void validaCriacao(Solicitacao solicitacao) {
        var cnpj = solicitacao.getCnpj();
        var tipoSolicitacao = solicitacao.getTipoSolicitacao();
        var solicitacaoExistente = repository.findByCnpjAndTipoSolicitacaoAndStatusIn(cnpj, tipoSolicitacao, SolicitacaoStatus.emAberto());
        if (solicitacaoExistente.isPresent()) {
            throw BusinessException.createConflictBusinessException("Já solicitação ativa para este CNPJ e tipo de solicitação.");
        }
    }

    public List<DashboardResponse> dashboard() {
        return repository.dashboard(SolicitacaoStatus.emAberto());
    }

    public void atualizaSolicitacoesDocumentacaoPendente() {
        LocalDateTime dataLimite = LocalDateTime.now().minusDays(30);
        var solicitacoes = repository.findByStatusAndLastModifiedDate(SolicitacaoStatus.DOCUMENTACAO_PENDENTE, dataLimite);
        Justificativa justificativa = justificativaRepository.findById(0L)
                .orElseGet(() -> {
                    Justificativa nova = new Justificativa();
                    nova.setId(0L);
                    nova.setDescricao("30 dias no status 'Documentação Pendente'");
                    return justificativaRepository.save(nova);
                });

        solicitacoes.forEach(solicitacao -> {
            solicitacao.setJustificativa(justificativa);
            solicitacao.setStatus(SolicitacaoStatus.NAO_AUTORIZADO);
        });
        repository.saveAll(solicitacoes);
    }
}