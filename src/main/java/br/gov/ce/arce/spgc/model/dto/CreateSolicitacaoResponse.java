package br.gov.ce.arce.spgc.model.dto;

import java.util.List;

public record CreateSolicitacaoResponse(
        Long id,
        String nomeEmpresa,
        String cnpj,
        String endereco,
        String telefone,
        String tipoSolicitacao,
        String email,
        String prepostoEmpresa,
        List<CreateSolicitacaoArquivoResponse> arquivos
) {}