package br.gov.ce.arce.spgc.model.dto;

import jakarta.validation.constraints.NotBlank;

public record AnalistaFinalizaSolicitacaoRequest(
        @NotBlank(message = "É necessario informar se a solicitação e valida ou não")
        boolean valido,
        String justificativa) {}
