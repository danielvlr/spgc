package br.gov.ce.arce.spgc.client.suite;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CriarProcessoSistemaExternoResponse {

    @JsonProperty("can_tramit")
    private Boolean canTramit;

    private String type; // initial, reference

    @JsonProperty("subject_id")
    private Integer subjectId;

    @JsonProperty("capacity_id")
    private Integer capacityId;

    @JsonProperty("competent_part_id")
    private Integer competentPartId;

    private Integer origin;

    private String nup;

    private String date; // 2024-04-26T13:37:56.829556-03:00

    @JsonProperty("reference_number")
    private String referenceNumber;

    @JsonProperty("priority_id")
    private Integer priorityId;

    @JsonProperty("secrecy_id")
    private Integer secrecyId;

    @JsonProperty("external_status")
    private String externalStatus;

    @JsonProperty("external_system_name")
    private String externalSystemName;

    @JsonProperty("external_system_abbreviation")
    private String externalSystemAbbreviation;

    @JsonProperty("process_id")
    private String processId;

}
