package br.gov.ce.arce.spgc.model.dto;

import br.gov.ce.arce.spgc.model.enumeration.TipoSolicitacao;

public record DashboardResponse(
        TipoSolicitacao tipoSolicitacao,
        Long quantidade
) {}

