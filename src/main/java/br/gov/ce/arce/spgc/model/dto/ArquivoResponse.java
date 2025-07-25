package br.gov.ce.arce.spgc.model.dto;

public record ArquivoResponse(
        Long id,
        String tipoDocumento,
        String url,
        Boolean valido,
        String justificativa
) {}

