package br.gov.ce.arce.spgc.model.mapper;

import br.gov.ce.arce.spgc.model.dto.*;
import br.gov.ce.arce.spgc.model.entity.Arquivo;
import br.gov.ce.arce.spgc.model.entity.Solicitacao;
import br.gov.ce.arce.spgc.model.enumeration.TipoDocumento;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ArquivoMapper {

    @Mapping(target = "tipoDocumento", source = "tipoDocumento", qualifiedByName = "tipoDocumentoToString")
    ArquivoResponse toArquivoResponse(Arquivo entity);

    @Named("tipoDocumentoToString")
    default String stringToTipoDocumento(TipoDocumento tipoDocumento) {
        return tipoDocumento.name();
    }
}