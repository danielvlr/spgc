package br.gov.ce.arce.spgc.client.suite;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CriarProcessoExternoResponse {

    public CriarProcessoExternoResponse() {
        this.nup = "";
    }

    @JsonProperty("user_id")
    private Integer userId;

    @JsonProperty("user_name")
    private String userName;

    private String type; // initial, reference

    @JsonProperty("subject_id")
    private Integer subjectId;

    @JsonProperty("subject_description")
    private String subjectDescription;

    @JsonProperty("capacity_id")
    private Integer capacityId;

    private String nup;

    private String date; // 2024-04-25T19:55:51.561244-03:00

    private Integer origin;

    @JsonProperty("origin_description")
    private String originDescription;

    @JsonProperty("priority_id")
    private Integer priorityId;

    @JsonProperty("secrecy_id")
    private Integer secrecyId;

    @JsonProperty("competent_part_id")
    private Integer competentPartId;

    @JsonProperty("can_tramit")
    private Boolean canTramit;

    @JsonProperty("process_reference")
    private List<String> processReference; // uuid

    @JsonProperty("reference_number")
    private String referenceNumber;

    private String observation;

    @JsonProperty("process_id")
    private String processId;

}
