package br.gov.ce.arce.spgc.controller;

import br.gov.ce.arce.spgc.model.BaseResponse;
import br.gov.ce.arce.spgc.model.dto.ArquivoResponse;
import br.gov.ce.arce.spgc.model.dto.ValidaArquivoRequest;
import br.gov.ce.arce.spgc.service.ArquivoService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static br.gov.ce.arce.spgc.model.BaseResponse.okSuccess;

@RestController
@RequestMapping("arquivo")
@AllArgsConstructor
public class Arquivo extends BaseController {

    private final ArquivoService service;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<ArquivoResponse> findById(@PathVariable Long id) {
        return okSuccess(service.findById(id));
    }

    @PatchMapping("/valida")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<BaseResponse<ArquivoResponse>> validaArquivo(@RequestBody ValidaArquivoRequest payload) {
        return okResponseEntity(service.validaArquivo(payload));
    }
}
