package br.gov.ce.arce.spgc.service;

import br.gov.ce.arce.spgc.client.minio.MinioService;
import br.gov.ce.arce.spgc.exception.BusinessException;
import br.gov.ce.arce.spgc.model.BasePageResponse;
import br.gov.ce.arce.spgc.model.dto.*;
import br.gov.ce.arce.spgc.model.entity.Solicitacao;
import br.gov.ce.arce.spgc.model.enumeration.SolicitacaoStatus;
import br.gov.ce.arce.spgc.model.mapper.SolicitacaoMapper;
import br.gov.ce.arce.spgc.repository.SolicitacaoRepository;
import br.gov.ce.arce.spgc.strategy.SolicitacaoStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

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
        solicitacao.getArquivos().stream().forEach(arquivo->{
            var url = minioService.saveFileBase64(arquivo.getConteudoBase64(), "teste", arquivo.getTipoDocumento().name(),arquivo.getId());
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
    public boolean validaDocumentos(Solicitacao solicitacao, Predicate<Boolean> predicate) {
        return solicitacao.getArquivos().stream()
                .allMatch(a -> predicate.test(a.getValido()));
    }

    public boolean validaTodosDocumentosAvaliados(Solicitacao solicitacao) {
        return validaDocumentos(solicitacao, Objects::nonNull);
    }

    public boolean validaTodosDocumentosConfirmados(Solicitacao solicitacao) {
        return validaDocumentos(solicitacao, Boolean.TRUE::equals);
    }

    public SolicitacaoResponse analistaFinalizaSolicitacaoRequest(Long id, AnalistaFinalizaSolicitacaoRequest payload) {
        var solicitacao = repository.getReferenceById(id);
        var strategy = getStrategy(solicitacao.getTipoSolicitacao().name());

        if (!validaTodosDocumentosAvaliados(solicitacao)) {
            throw BusinessException.createBadRequestBusinessException("Solicitação ainda tem arquivos pendentes de análise.");
        }

        if (!validaTodosDocumentosConfirmados(solicitacao)) {
            throw BusinessException.createBadRequestBusinessException("Solicitação tem arquivos não autorizados.");
        }

        solicitacao.setJustificativa(payload.justificativa());

        if(payload.valido()) {
            solicitacao.setStatus(strategy.analistaFinalizaSolicitacao());
        }else {
            solicitacao.setStatus(SolicitacaoStatus.DOCUMENTACAO_PENDENTE);
            emailSpgcService.enviaEmailPendenciaDocumentoSolicitante(solicitacao);
        }

        if (solicitacao.getStatus() == SolicitacaoStatus.CONCLUIDO) {
            emailSpgcService.enviaEmailSolicitacaoConcluida(solicitacao);
        }

        return mapper.toSolicitacaoResponse(repository.save(solicitacao));
    }

    public SolicitacaoResponse conselhorDiretorFinalizaSolicitacaoRequest(Long id, ConselhoDiretorFinalizaSolicitacaoRequest payload) {
        var solicitacao = repository.getReferenceById(id);

        if (solicitacao.getNup() == null || solicitacao.getNup().isBlank()) {
            throw BusinessException.createBadRequestBusinessException("Solicitação não possui NUP preenchido.");
        }

        if (solicitacao.getStatus() != SolicitacaoStatus.EM_ANALISE_ASSESSORIA) {
            throw BusinessException.createBadRequestBusinessException("Solicitação não está no status 'EM_ANALISE_ASSESSORIA'.");
        }

        solicitacao.setJustificativa(payload.justificativa());

        if(payload.valido()){
            solicitacao.setStatus(SolicitacaoStatus.CONCLUIDO);
            emailSpgcService.enviaEmailSolicitacaoConcluida(solicitacao);
        }else {
            solicitacao.setStatus(SolicitacaoStatus.REJEITADO);
            emailSpgcService.enviaEmailSolicitacaoRejeitada(solicitacao);
        }
        return mapper.toSolicitacaoResponse(repository.save(solicitacao));
    }

    void validaCriacao(Solicitacao solicitacao) {
        var cnpj = solicitacao.getCnpj();
        var tipoSolicitacao = solicitacao.getTipoSolicitacao();
        var solicitacaoExistente = repository.findByCnpjAndTipoSolicitacaoAndStatusNotIn(cnpj, tipoSolicitacao, SolicitacaoStatus.emAberto());

        if (solicitacaoExistente.isPresent()) {
            throw BusinessException.createConflictBusinessException("Já existe uma solicitação ativa para este CNPJ e tipo de solicitação.");
        }
    }

    public List<DashboardResponse> dashboard() {
        return repository.dashboard();
    }
}