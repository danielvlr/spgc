package br.gov.ce.arce.spgc.model.dto;

import java.util.List;

public record SolicitacaoResponse(
        Long id,
        String nomeEmpresa,
        String cnpj,
        String endereco,
        String telefone,
        String tipoSolicitacao,
        String email,
        String prepostoEmpresa,
        String status,
        String token,
        String nupSuite,
        List<ArquivoResponse> arquivos
) {}