package br.gov.ce.arce.spgc.client.suite;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@FeignClient(contextId = "suiteClient", name = "suite-client", url = "${suite.api.url}", configuration = SuiteClientConfiguration.class)
public interface SuiteClient {

    @GetMapping(path = "/process/doe/sgdo/{nup}")
    ResponseEntity<SuiteResponse> getInfoSuiteByNup(@PathVariable("nup") String nup);

    @PutMapping(path = "/autorizar/sgdo/{nup}")
    ResponseEntity<Void> autorizarDocumentos(@PathVariable("nup") String nup, @RequestBody AutorizacaoDocumentos autorizacaoDocumentos);

}
