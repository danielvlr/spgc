package br.gov.ce.arce.spgc.service;

import br.gov.ce.arce.spgc.client.minio.MinioService;
import br.gov.ce.arce.spgc.model.dto.*;
import br.gov.ce.arce.spgc.model.enumeration.SolicitacaoStatus;
import br.gov.ce.arce.spgc.model.mapper.ArquivoMapper;
import br.gov.ce.arce.spgc.repository.ArquivoRepository;
import br.gov.ce.arce.spgc.repository.SolicitacaoRepository;
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
    private final SolicitacaoRepository solicitacaoRepository;


    public ArquivoResponse findById(Long id) {
        return mapper.toArquivoResponse(repository.getReferenceById(id));
    }

    @Transactional
    public ArquivoResponse analistaValidaArquivo(ValidaArquivoRequest request) {
        var arquivo = repository.getReferenceById(request.id());

        // Salva dados arquivo
        arquivo.setValido(request.valido());
        arquivo.setJustificativa(request.justificativa());
        repository.save(arquivo);

        return mapper.toArquivoResponse(arquivo);
    }

    @Transactional
    public ArquivoResponse atualizaArquivoSolicitante(Long id, ArquivoRequest payload) {
        var arquivo = repository.getReferenceById(id);
        var solicitacao = arquivo.getSolicitacao();

        // verifica se todos os arquivos pendentes de envio foram atualizado
        boolean todosAtualizados = solicitacao.getArquivos().stream()
                .allMatch(a -> a.getValido() != null && solicitacao.getStatus() == SolicitacaoStatus.DOCUMENTACAO_PENDENTE);
        if(todosAtualizados){
            solicitacao.setStatus(SolicitacaoStatus.EM_ANALISE);
            emailSpgcService.enviaEmailConfirmacaoCentral(solicitacao);
        }

        // atualiza dados campo de controle para null depois que o usuario subir novo arquivo
        arquivo.setJustificativa(null);
        arquivo.setValido(null);

        var url = minioService.saveFileBase64(payload.conteudoBase64(), "teste", arquivo.getTipoDocumento().name(),arquivo.getId());
        arquivo.setUrl(url);
        return mapper.toArquivoResponse(repository.save(arquivo));
    }
}