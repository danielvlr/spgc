package br.gov.ce.arce.spgc.model.dto.suite;

import lombok.Data;

import java.util.List;

@Data
public class ErroSuiteTO {

    private String detail;
    private List<String> message;

}
