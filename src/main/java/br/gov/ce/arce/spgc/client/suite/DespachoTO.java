package br.gov.ce.arce.spgc.client.suite;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DespachoTO {

    private String capacity;

    private String origin;

    private String responsavel;

    private Integer suiteProcessoId;

    @JsonProperty("processed_at")
    private String processedAt;

}
