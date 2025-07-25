package br.gov.ce.arce.spgc.client.suite;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileSuiteResponse {

    @JsonProperty("document_type")
    public String documentType;

    @JsonProperty("file")
    public String file;

    @JsonProperty("id")
    public String id;

}
