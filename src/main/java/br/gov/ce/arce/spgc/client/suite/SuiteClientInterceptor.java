package br.gov.ce.arce.spgc.client.suite;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;

public class SuiteClientInterceptor implements RequestInterceptor {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String ACCEPT_HEADER = "Accept";

    @Value(value = "${suite.api.token}")
    private String tokenSuite;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String token;

        if (!requestTemplate.headers().containsKey(AUTHORIZATION_HEADER)) {
            requestTemplate.header(AUTHORIZATION_HEADER, tokenSuite);
            requestTemplate.header(ACCEPT_HEADER,"application/json;version=1.0");
        }

    }

}
