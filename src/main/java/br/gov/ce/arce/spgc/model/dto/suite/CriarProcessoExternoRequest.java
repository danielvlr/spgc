package br.gov.ce.arce.spgc.model.dto.suite;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Data
public class CriarProcessoExternoRequest {

    @JsonProperty("user_id")
    private Integer userId; // id do usuário que será adicionado como interessado

    private String name; // nome do usuário que será adicionado como interessado

    private List<CcExternoTO> cc; // lista de interessados

    @JsonProperty("subject_id")
    @NotNull
    private Integer subjectId; // id do assunto do processo

    @JsonProperty("origin_id")
    @NotNull
    private Integer originId; // id da lotação de abertura

    @JsonProperty("capacity_id")
    @NotNull
    private Integer capacityId; // id da lotação que o processo será tramitado

    @JsonProperty("external_number")
    private String externalNumber; // número do processo na integração

    @Builder.Default
    @NotNull
    private List<ArquivoExternoTO> files = new ArrayList<>();

    @JsonProperty("process_reference_list")
    private List<String> processReferenceList; // lista de nups de referência

    @JsonProperty("system_name")
    private String systemName; // nome do sistema que está integrando

    @JsonProperty("system_abbreviation_name")
    private String systemAbbreviationName; // abreviação do sistema que está integrando

}
