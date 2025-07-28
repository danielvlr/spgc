package br.gov.ce.arce.spgc.service;

import br.gov.ce.arce.spgc.client.minio.MinioService;
import br.gov.ce.arce.spgc.exception.BusinessException;
import br.gov.ce.arce.spgc.model.dto.*;
import br.gov.ce.arce.spgc.model.entity.Solicitacao;
import br.gov.ce.arce.spgc.model.enumeration.SolicitacaoStatus;
import br.gov.ce.arce.spgc.model.mapper.ArquivoMapper;
import br.gov.ce.arce.spgc.model.mapper.JustificativaMapper;
import br.gov.ce.arce.spgc.repository.ArquivoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ArquivoService {

    private final ArquivoRepository repository;
    private final ArquivoMapper mapper;
    private final EmailSpgcService emailSpgcService;
    private final MinioService minioService;
    private final SolicitacaoService solicitacaoService;
    private final JustificativaMapper justificativaMapper;

    public ArquivoResponse findById(Long id) {
        return mapper.toArquivoResponse(repository.getReferenceById(id));
    }

    @Transactional
    public ArquivoResponse analistaValidaArquivo(ValidaArquivoRequest request) {
        var arquivo = repository.getReferenceById(request.id());
        var solicitacao = arquivo.getSolicitacao();
        var justificativa = justificativaMapper.toEntity(request.justificativa());

        // Valida arquivo e solicitacao
        valida(solicitacao);

        // Salva dados arquivo
        arquivo.setAprovado(request.aprovado());
        arquivo.setJustificativa(justificativa);

        repository.save(arquivo);

        // Verifica se deve atualizar status solicitacao
        solicitacaoService.todosDocumentosAnalisadosSolicitacao(solicitacao);

        return mapper.toArquivoResponse(arquivo);
    }

    private void valida(Solicitacao solicitacao) {
        var status = solicitacao.getStatus();

        if (!SolicitacaoStatus.emAberto().contains(status)) {
            throw BusinessException.createBadRequestBusinessException(
                    "Não é possível alterar solicitações em status final.");
        }

        if (status == SolicitacaoStatus.AGUARDANDO_ANALISE) {
            solicitacao.setStatus(SolicitacaoStatus.EM_ANALISE);
            emailSpgcService.enviaEmailEmAnaliseSolicitante(solicitacao);
            return;
        }

        if (status == SolicitacaoStatus.DOCUMENTACAO_PENDENTE
                && solicitacaoService.validaTodosDocumentosAvaliadosOuPendenteAnalista(solicitacao)) {
            solicitacao.setStatus(SolicitacaoStatus.EM_ANALISE);
            emailSpgcService.enviaEmailEmAnaliseSolicitante(solicitacao);
        } else{
            throw BusinessException.createBadRequestBusinessException("Não é possivel atualizar solicitação pois o solicitante ainda tem documentos pendentes.");
        }
    }

    @Transactional
    public ArquivoResponse atualizaArquivoSolicitante(Long id, ArquivoRequest payload) {
        var arquivo = repository.getReferenceById(id);
        var solicitacao = arquivo.getSolicitacao();

        // Verifica se o arquivo ja e valido
        if(arquivo.getAprovado() != null && arquivo.getAprovado()){
            throw BusinessException.createConflictBusinessException("Arquivo ja esta com status valido.");
        }

        // atualiza dados campo de controle para null depois que o usuario subir novo arquivo
        arquivo.setJustificativa(null);
        arquivo.setAprovado(null);

        // verifica se todos os arquivos pendentes de envio foram atualizado
        boolean todosAtualizados = solicitacao.getArquivos().stream()
                .allMatch(a -> (a.getAprovado() == null || a.getAprovado()) && solicitacao.getStatus() == SolicitacaoStatus.DOCUMENTACAO_PENDENTE);

        if(todosAtualizados){
            solicitacao.setStatus(SolicitacaoStatus.EM_ANALISE);
            emailSpgcService.enviaEmailConfirmacaoCentral(solicitacao);
        }

        var url = minioService.saveFileBase64(payload.conteudoBase64(), "teste", arquivo.getTipoDocumento().name(),arquivo.getId());
        arquivo.setUrl(url);
        return mapper.toArquivoResponse(repository.save(arquivo));
    }
}