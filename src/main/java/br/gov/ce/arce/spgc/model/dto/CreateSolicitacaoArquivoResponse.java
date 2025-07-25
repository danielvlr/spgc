package br.gov.ce.arce.spgc.model.dto;

public record CreateSolicitacaoArquivoResponse(
        Long id,
        String tipoDocumento,
        String url) {}