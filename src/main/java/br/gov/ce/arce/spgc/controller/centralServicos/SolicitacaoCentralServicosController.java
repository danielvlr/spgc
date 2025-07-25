package br.gov.ce.arce.spgc.controller.centralServicos;

import br.gov.ce.arce.spgc.controller.BaseController;
import br.gov.ce.arce.spgc.model.BaseResponse;
import br.gov.ce.arce.spgc.model.dto.*;
import br.gov.ce.arce.spgc.service.ArquivoService;
import br.gov.ce.arce.spgc.service.SolicitacaoService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static br.gov.ce.arce.spgc.model.BaseResponse.okSuccess;

@RestController
@RequestMapping("/centralServicos/solicitacao")
@AllArgsConstructor
public class SolicitacaoCentralServicosController extends BaseController {

    private final SolicitacaoService solicitacaoService;
    private final ArquivoService arquivoService;

    @PostMapping
    @Operation(summary = "Criar solicitação de autorização", description = "Criar solicitação de autorização para atuar no mercado livre de gás")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BaseResponse<SolicitacaoResponse>> create(@Valid @RequestBody CreateSolicitacaoRequest payload){
        var solicitacaoResponse = this.solicitacaoService.create(payload);
        return okResponseEntity(solicitacaoResponse, "Informe Regulação criado com sucesso");
    }

    @PostMapping("/arquivo/{id}")
    @Operation(summary = "Atualiza arquivo rejeitado", description = "Atualiza arquivo rejeitado")
    public ResponseEntity<BaseResponse<ArquivoResponse>> update(@Valid @RequestBody ArquivoRequest payload,
                                                                @PathVariable Long id){
        var solicitacaoResponse = this.arquivoService.update(id, payload);
        return okResponseEntity(solicitacaoResponse, "Informe Regulação criado com sucesso");
    }


    @GetMapping
    public BaseResponse<List<SolicitacaoResponse>> findById(@RequestParam String cnpj) {
        var result = this.solicitacaoService.findByCnpj(cnpj);
        return okSuccess(result);
    }
}
