package br.gov.ce.arce.spgc.model.mapper;

import br.gov.ce.arce.spgc.model.dto.*;
import br.gov.ce.arce.spgc.model.entity.Arquivo;
import br.gov.ce.arce.spgc.model.entity.Solicitacao;
import br.gov.ce.arce.spgc.model.enumeration.TipoSolicitacao;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Objects;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface SolicitacaoMapper {

    @Mapping(source = "cnpj", target = "cnpj", qualifiedByName = "apenasNumeros")
    @Mapping(source = "tipoSolicitacao", target = "tipoSolicitacao", qualifiedByName = "tipoSolicitacao")
    @Mapping(target = "status", constant = "AGUARDANDO_ANALISE")
    Solicitacao toEntity(CreateSolicitacaoRequest request);

    @Mapping(source = "cnpj", target = "cnpj", qualifiedByName = "apenasNumeros")
    @Mapping(source = "tipoSolicitacao", target = "tipoSolicitacao", qualifiedByName = "tipoSolicitacao")
    Solicitacao toEntity(SolicitacaoRequest request);

    @Mapping(source = "status", target = "status", qualifiedByName = "statusToString")
    @Mapping(source = "tipoSolicitacao", target = "tipoSolicitacao", qualifiedByName = "tipoSolicitacaoToString")
    SolicitacaoResponse toSolicitacaoResponse(Solicitacao entity);
    List<SolicitacaoResponse> toSolicitacaoResponseList(List<Solicitacao> entitys);

    @Named("apenasNumeros")
    static String apenasNumeros(String cnpj) {
        return cnpj == null ? null : cnpj.replaceAll("\\D", "");
    }

    @Named("tipoSolicitacao")
    static TipoSolicitacao tipoSolicitacao(String tipoSolicitacao) {
        return Objects.isNull(tipoSolicitacao) ? null : TipoSolicitacao.valueOf(tipoSolicitacao);
    }

    @Named("statusToString")
    static String statusToString(br.gov.ce.arce.spgc.model.enumeration.SolicitacaoStatus status){
        return status == null ? null : status.name();
    }

    @Named("tipoSolicitacaoToString")
    static String tipoSolicitacaoToString(TipoSolicitacao tipoSolicitacao){
        return tipoSolicitacao.name();
    }

    @AfterMapping
    default void linkSolicitacao(@MappingTarget Solicitacao solicitacao) {
        if (solicitacao.getArquivos() != null) {
            for (Arquivo a : solicitacao.getArquivos()) {
                a.setSolicitacao(solicitacao);
            }
        }
    }

}