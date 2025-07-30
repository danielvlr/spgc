package br.gov.ce.arce.spgc.client.suite;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Data
public class TramitarProcessoTO {

    @JsonProperty("capacity_id")
    @NotNull
    private Integer capacityId;

    @NotEmpty
    private String status; // Aguardando análise, Em análise, Desarquivado, Arquivado, Tramitado (TramitacaoStatusEnum)

}
