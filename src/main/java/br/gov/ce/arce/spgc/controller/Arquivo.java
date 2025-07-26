package br.gov.ce.arce.spgc.controller;

import br.gov.ce.arce.spgc.model.BaseResponse;
import br.gov.ce.arce.spgc.model.dto.ArquivoResponse;
import br.gov.ce.arce.spgc.model.dto.ValidaArquivoRequest;
import br.gov.ce.arce.spgc.service.ArquivoService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static br.gov.ce.arce.spgc.model.BaseResponse.okSuccess;

@RestController
@RequestMapping("arquivo")
@AllArgsConstructor
public class Arquivo extends BaseController {

    private final ArquivoService service;

    @GetMapping("/{id}")
    public BaseResponse<ArquivoResponse> findById(@PathVariable Long id) {
        return okSuccess(service.findById(id));
    }

    @PatchMapping("/analista-valida-arquivo")
    public ResponseEntity<BaseResponse<ArquivoResponse>> analistaValidaArquivo(@RequestBody ValidaArquivoRequest payload) {
        return okResponseEntity(service.analistaValidaArquivo(payload));
    }
}
