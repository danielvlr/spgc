package br.gov.ce.arce.spgc.model.dto;

import jakarta.validation.constraints.NotNull;

public record JustificativaRequest(
        @NotNull(message = "Id é obrigatório")
        Long id,
        String descricao
) {}