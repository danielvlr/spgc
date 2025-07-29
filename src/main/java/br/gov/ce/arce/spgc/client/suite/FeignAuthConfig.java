package br.gov.ce.arce.spgc.client.suite;

import br.gov.ce.arce.spgc.util.AppSuiteProperties;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class FeignAuthConfig {

    private final AppSuiteProperties appSuiteProperties;

    @Bean
    public RequestInterceptor bearerTokenInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                template.header("Authorization", "Bearer " + appSuiteProperties
                        .getPapelZeroApi().getAccessToken());
            }
        };
    }
}
