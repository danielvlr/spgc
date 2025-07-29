package br.gov.ce.arce.spgc.model.dto.suite;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;


@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Data
public class CriarProcessoSistemaExternoRequest {

    @JsonProperty("subject_id")
    @NotNull
    private Integer subjectId; // assunto do processo

    @JsonProperty("origin_id")
    @NotNull
    private Integer originId; // lotação de abertura

    @JsonProperty("capacity_id")
    @NotNull
    private Integer capacityId; // lotação que o processo será tramitado

    @JsonProperty("external_number")
    private String externalNumber; // número do processo na integração

    @JsonProperty("system_name")
    @NotBlank
    private String systemName; // nome do sistema que está integrando

    @JsonProperty("system_abbreviation")
    @NotBlank
    private String systemAbbreviation; // abreviação do sistema que está integrando

}
