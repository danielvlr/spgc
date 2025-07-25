package br.gov.ce.arce.spgc.model.dto;

import jakarta.validation.constraints.NotBlank;

public record ArquivoRequest(
        @NotBlank(message = "O conteúdo do arquivo (base64) é obrigatório")
        String conteudoBase64
) {}