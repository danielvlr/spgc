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
@RequestMapping("/central-servicos")
@AllArgsConstructor
public class SolicitacaoCentralServicosController extends BaseController {

    private final SolicitacaoService solicitacaoService;
    private final ArquivoService arquivoService;

    @PostMapping("/solicitacao")
    @Operation(summary = "Criar solicitação de autorização", description = "Criar solicitação de autorização para atuar no mercado livre de gás")
    public ResponseEntity<BaseResponse<SolicitacaoResponse>> criarSolicitacao(@Valid @RequestBody CreateSolicitacaoRequest payload){
        var solicitacaoResponse = this.solicitacaoService.criarSolicitacao(payload);
        return okResponseEntity(solicitacaoResponse, "Informe Regulação criado com sucesso");
    }

    @GetMapping("/solicitacao")
    public BaseResponse<List<SolicitacaoResponse>> findByCnpj(@RequestParam String cnpj) {
        var result = this.solicitacaoService.findByCnpj(cnpj);
        return okSuccess(result);
    }

    @PatchMapping("/arquivo/{id}")
    @Operation(summary = "Atualiza arquivo rejeitado", description = "Atualiza arquivo rejeitado")
    public ResponseEntity<BaseResponse<ArquivoResponse>> atualizaArquivoSolicitante(@Valid @RequestBody ArquivoRequest payload,
                                                                @PathVariable Long id){
        var solicitacaoResponse = this.arquivoService.atualizaArquivoSolicitante(id, payload);
        return okResponseEntity(solicitacaoResponse, "Atualização status arquivo realizada com sucesso");
    }
}
