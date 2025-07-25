package br.gov.ce.arce.spgc.service;

import br.gov.ce.arce.spgc.client.minio.MinioService;
import br.gov.ce.arce.spgc.exception.BusinessException;
import br.gov.ce.arce.spgc.model.BasePageResponse;
import br.gov.ce.arce.spgc.model.dto.*;
import br.gov.ce.arce.spgc.model.entity.Arquivo;
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

@Service
@RequiredArgsConstructor
public class SolicitacaoService {

    private final MinioService minioService;
    private final SolicitacaoRepository repository;
    private final SolicitacaoMapper mapper;
    private final EmailSpgcService emailSpgcService;
    private final Map<String, SolicitacaoStrategy> strategyMap;

    public SolicitacaoResponse create(CreateSolicitacaoRequest request) {
        SolicitacaoStrategy strategy = getStrategy(request.tipoSolicitacao());

        // converte o request em entity
        var solicitacao = mapper.toEntity(request);

        // Valida de acordo com o tipo de documento
        strategy.valida(solicitacao);

        // Valida regra geral
        valida(solicitacao);

        // Salvando solicitacao para gerar os Id na base de dados
        solicitacao = repository.save(solicitacao);

        // Envia arquivo para o minio
        solicitacao.getArquivos().stream().forEach(arquivo->{
            var url = minioService.saveFileBase64(arquivo.getConteudoBase64(), "teste", arquivo.getTipoDocumento().name(),arquivo.getId());
            arquivo.setUrl(url);
        });

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

    private void valida(Solicitacao solicitacao) {
        var cnpj = solicitacao.getCnpj();
        var tipoSolicitacao = solicitacao.getTipoSolicitacao();
        var solicitacaoExistente = repository.findByCnpjAndTipoSolicitacaoAndStatusNotIn(cnpj, tipoSolicitacao, SolicitacaoStatus.emAberto());

        if (solicitacaoExistente.isPresent()) {
            throw BusinessException.createConflictBusinessException("Já existe uma solicitação ativa para este CNPJ e tipo de solicitação.");
        }
    }

    public void atualizaStatusSolicitacao(Solicitacao solicitacao) {

        // Verifica se TODOS os arquivos da solicitação estão válidos
        boolean todosValidos = solicitacao.getArquivos()
                .stream()
                .allMatch(Arquivo::getValido);

        if (todosValidos) {
            solicitacao.setStatus(SolicitacaoStatus.CONCLUIDO);
            repository.save(solicitacao);
        }
    }
}