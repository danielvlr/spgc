package br.gov.ce.arce.spgc.util;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Data
@Component
@Validated
@ConfigurationProperties("app.spgc")
public class AppSuiteProperties {

    @NotNull
    private PapelZeroApiProperties papelZeroApi;

    @Data
    public static class PapelZeroApiProperties {

        @NotEmpty
        private String baseUrl;

        @NotEmpty
        private String accessToken;

        @NotNull
        private Integer assuntoIdGasCanalizado; // REGULAÇÃO - GÁS CANALIZADO

        @NotNull
        private Integer lotacaoIdOrigem; // Assessoria de Gabinete e Controle Interno da ARCE (CDR/AGC)

        @NotNull
        private Integer lotacaoIdDestino; // Assessoria de Gabinete e Controle Interno da ARCE (CDR/AGC)

    }

}
