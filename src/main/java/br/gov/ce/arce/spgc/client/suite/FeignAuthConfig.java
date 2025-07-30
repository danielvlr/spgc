package br.gov.ce.arce.spgc.client.suite;

import br.gov.ce.arce.spgc.util.AppSuiteProperties;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@Configuration
@AllArgsConstructor
public class FeignAuthConfig {

    private final AppSuiteProperties appSuiteProperties;

    @Bean
    public RequestInterceptor bearerTokenInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                template.header(HttpHeaders.AUTHORIZATION, "Token " + appSuiteProperties.getAccessToken());
                template.header(HttpHeaders.ACCEPT, "application/json;version=1.0");
                template.header(HttpHeaders.CONTENT_TYPE, "application/json;version=1.0");
            }
        };
    }
}
