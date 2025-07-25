package br.gov.ce.arce.spgc.service;

import br.gov.ce.arce.spgc.client.minio.MinioService;
import br.gov.ce.arce.spgc.model.dto.*;
import br.gov.ce.arce.spgc.model.entity.Arquivo;
import br.gov.ce.arce.spgc.model.mapper.ArquivoMapper;
import br.gov.ce.arce.spgc.repository.SolicitacaoArquivoRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArquivoService {

    private final SolicitacaoArquivoRepository repository;
    private final ArquivoMapper mapper;
    private final EmailSpgcService emailSpgcService;
    private final SolicitacaoService solicitacaoService;
    private final MinioService minioService;


    public ArquivoResponse findById(Long id) {
        return mapper.toArquivoResponse(repository.getReferenceById(id));
    }

    public ArquivoResponse validaArquivo(ValidaArquivoRequest request) {
        var arquivo = repository.getReferenceById(request.id());
        var solicitacao = arquivo.getSolicitacao();

        // Salva dados arquivo
        salvaArquivo(request, arquivo);

        // Envia email caso documento nao seja valido
        enviaEmailSolicitante(arquivo);

        // Verifica se todos os arquivos estão validados
        solicitacaoService.atualizaStatusSolicitacao(solicitacao);

        return mapper.toArquivoResponse(arquivo);
    }

    private void enviaEmailSolicitante(Arquivo arquivo) {
        if(!arquivo.getValido()){
            emailSpgcService.enviaEmailSolicitante(arquivo);
        }
    }

    private void salvaArquivo(ValidaArquivoRequest request, Arquivo arquivo) {
        arquivo.setValido(request.valido());
        arquivo.setJustificativa(request.justificativa());
        repository.save(arquivo);
    }

    public ArquivoResponse update(Long id, ArquivoRequest payload) {
        var arquivo = repository.getReferenceById(id);
        arquivo.setJustificativa(null);
        arquivo.setValido(null);
        var url = minioService.saveFileBase64(payload.conteudoBase64(), "teste", arquivo.getTipoDocumento().name(),arquivo.getId());
        arquivo.setUrl(url);
        return mapper.toArquivoResponse(repository.save(arquivo));
    }
}