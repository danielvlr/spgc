package br.gov.ce.arce.spgc.client.suite;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SuiteResponse {

    @JsonProperty("resume_file")
    public String resumeFile;

    @JsonProperty("nup")
    public String nup;

    @JsonProperty("files")
    public List<FileSuiteResponse> files;

}
