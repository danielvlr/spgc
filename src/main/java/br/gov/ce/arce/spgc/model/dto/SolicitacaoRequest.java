package br.gov.ce.arce.spgc.model.dto;

public record SolicitacaoRequest(
        Long id,
        String nomeEmpresa,
        String cnpj,
        String endereco,
        String telefone,
        String tipoSolicitacao,
        String email,
        String prepostoEmpresa) { }
