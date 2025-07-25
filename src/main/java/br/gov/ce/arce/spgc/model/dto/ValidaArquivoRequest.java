package br.gov.ce.arce.spgc.model.dto;

import jakarta.validation.constraints.NotBlank;

public record ValidaArquivoRequest(
        @NotBlank(message = "Valor 'arquivoId' é obrigatório")
        Long id,

        @NotBlank(message = "Valor 'valido' é obrigatório")
        Boolean valido,

        String justificativa
) {}