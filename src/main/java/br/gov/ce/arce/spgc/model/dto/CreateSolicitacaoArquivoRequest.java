package br.gov.ce.arce.spgc.model.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateSolicitacaoArquivoRequest(
        @NotBlank(message = "O tipo do documento é obrigatório")
        String tipoDocumento,

        @NotBlank(message = "O conteúdo do arquivo (base64) é obrigatório")
        String conteudoBase64
) {}