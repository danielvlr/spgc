package br.gov.ce.arce.spgc.component;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.mail")
@Getter @Setter
public class EmailProperties {
    private String url;
    private String from;
}
