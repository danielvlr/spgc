package br.gov.ce.arce.spgc.model.mapper;

import br.gov.ce.arce.spgc.model.dto.*;
import br.gov.ce.arce.spgc.model.entity.Arquivo;
import br.gov.ce.arce.spgc.model.entity.Solicitacao;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface SolicitacaoMapper {

    @Mapping(source = "cnpj", target = "cnpj", qualifiedByName = "apenasNumeros")
    @Mapping(target = "status", constant = "AGUARDANDO_ANALISE")
    Solicitacao toEntity(CreateSolicitacaoRequest request);
    Solicitacao toEntity(SolicitacaoRequest request);

    SolicitacaoResponse toSolicitacaoResponse(Solicitacao entity);
    List<SolicitacaoResponse> toSolicitacaoResponseList(List<Solicitacao> entitys);

    @Named("apenasNumeros")
    static String apenasNumeros(String cnpj) {
        return cnpj == null ? null : cnpj.replaceAll("\\D", "");
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