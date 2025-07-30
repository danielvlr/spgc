package br.gov.ce.arce.spgc.client.suite;

import lombok.Data;

import java.util.List;

@Data
public class ErroSuiteTO {

    private String detail;
    private List<String> message;

}
