package br.gov.ce.arce.spgc.client.suite;

import br.gov.ce.arce.spgc.util.validation.CpfCnpj;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@Data
public class CcExternoTO {

    @JsonProperty("user_id")
    private Integer userId;

    private String name;

    @JsonProperty("cpf_cnpj")
    @CpfCnpj
    private String cpfCnpj;

}
