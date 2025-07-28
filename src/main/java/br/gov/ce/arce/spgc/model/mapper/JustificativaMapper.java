package br.gov.ce.arce.spgc.model.mapper;

import br.gov.ce.arce.spgc.model.dto.ArquivoResponse;
import br.gov.ce.arce.spgc.model.dto.CreateJustificativaRequest;
import br.gov.ce.arce.spgc.model.dto.JustificativaRequest;
import br.gov.ce.arce.spgc.model.dto.JustificativaResponse;
import br.gov.ce.arce.spgc.model.entity.Arquivo;
import br.gov.ce.arce.spgc.model.entity.Justificativa;
import br.gov.ce.arce.spgc.model.enumeration.TipoDocumento;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface JustificativaMapper {

    // to entity
    Justificativa toEntity(CreateJustificativaRequest request);
    Justificativa toEntity(JustificativaRequest request);

    // to response
    JustificativaResponse toResponse(Justificativa entity);

}