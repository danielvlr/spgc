package br.gov.ce.arce.spgc.model.dto.suite;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ProcessoSuiteTO {

    private String id;

    private String subject;

    private String priority;

    private String secrecy;

    private String capacity;

    private String nup;

    private List<String> cc;

    private String origin;

    private String status;

    @JsonProperty("is_archived")
    private Boolean isArchived;

    @JsonProperty("created_date")
    private String createdDate;

    @JsonProperty("created_username")
    private String createdUsername;

    private List<DespachoTO> dispatches;

}
