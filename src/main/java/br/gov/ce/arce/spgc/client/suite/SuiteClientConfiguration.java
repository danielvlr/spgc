package br.gov.ce.arce.spgc.client.suite;

import org.springframework.context.annotation.Bean;

public class SuiteClientConfiguration {

    @Bean
    public SuiteClientInterceptor suiteClientInterceptor() {
        return new SuiteClientInterceptor();
    }

}
