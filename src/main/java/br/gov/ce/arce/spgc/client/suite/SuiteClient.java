package br.gov.ce.arce.spgc.client.suite;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@FeignClient(contextId = "suiteClient", name = "suite-client", url = "${app.spgc.papel-zero-api.base-url}",
        configuration = FeignAuthConfig.class)
public interface SuiteClient {

    @GetMapping(path = "/process/nup/{nup}")
    ResponseEntity<ProcessoSuiteTO> consultarProcessoNup(
            @NotEmpty @Size(max = 20, message = "inválido")
            @Pattern(regexp = "^[0-9]*$", message = "inválido")
            @PathVariable("nup") String nup);

    @PostMapping(path = "/process/create/external-system")
    ResponseEntity<CriarProcessoSistemaExternoResponse> criarProcessoSistemaExterno(
            @NotNull @Valid @RequestBody CriarProcessoSistemaExternoRequest request);

    @PostMapping(path = "/process/create/external")
    ResponseEntity<CriarProcessoExternoResponse> criarProcessoExterno(
            @NotNull @Valid @RequestBody CriarProcessoExternoRequest request);

    @PatchMapping(path = "/process/{nup}/tramitar")
    ResponseEntity<TramitarProcessoTO> tramitarProcesso(
            @NotEmpty @Size(max = 20, message = "inválido")
            @Pattern(regexp = "^[0-9]*$", message = "inválido")
            @PathVariable("nup") String nup,
            @NotNull @Valid @RequestBody TramitarProcessoTO request);
}

