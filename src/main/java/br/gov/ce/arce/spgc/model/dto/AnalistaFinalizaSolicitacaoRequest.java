package br.gov.ce.arce.spgc.model.dto;

import jakarta.validation.constraints.NotBlank;

public record AnalistaFinalizaSolicitacaoRequest(
        String justificativa) {}
