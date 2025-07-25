package br.gov.ce.arce.spgc.service;

import br.gov.ce.arce.spgc.client.minio.MinioService;
import br.gov.ce.arce.spgc.model.dto.*;
import br.gov.ce.arce.spgc.model.entity.Arquivo;
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
    public ArquivoResponse validaArquivo(ValidaArquivoRequest request) {
        var arquivo = repository.getReferenceById(request.id());

        // Salva dados arquivo
        arquivo.setValido(request.valido());
        arquivo.setJustificativa(request.justificativa());
        repository.save(arquivo);

        return mapper.toArquivoResponse(arquivo);
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