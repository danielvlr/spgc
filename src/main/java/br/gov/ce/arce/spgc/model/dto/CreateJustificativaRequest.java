package br.gov.ce.arce.spgc.model.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateJustificativaRequest(
        @NotBlank(message = "A descrição é obrigatório")
        String descricao
) {}