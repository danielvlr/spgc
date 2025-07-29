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
public class ArquivoExternoTO {

    @NotBlank
    private String file;

    @JsonProperty("document_type_id")
    @NotNull
    private Integer documentTypeId;

    @JsonProperty("file_name")
    @NotBlank
    private Integer fileName;

    @JsonProperty("file_type")
    @NotNull
    private String fileType; // url_file, base64 (TipoArquivoEnum)

}
