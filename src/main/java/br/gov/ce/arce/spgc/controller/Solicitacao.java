package br.gov.ce.arce.spgc.controller;

import br.gov.ce.arce.spgc.model.BasePageResponse;
import br.gov.ce.arce.spgc.model.BaseResponse;
import br.gov.ce.arce.spgc.model.dto.*;
import br.gov.ce.arce.spgc.service.SolicitacaoService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static br.gov.ce.arce.spgc.model.BaseResponse.okSuccess;

@RestController
@RequestMapping("solicitacao")
@AllArgsConstructor
public class Solicitacao extends BaseController {

    private final SolicitacaoService service;

    @GetMapping
    public ResponseEntity<BaseResponse<BasePageResponse<SolicitacaoResponse>>> findAll(
            @RequestParam(required = false) String nomeEmpresa,
            @RequestParam(required = false) String cnpj,
            @RequestParam(required = false) String endereco,
            @RequestParam(required = false) String telefone,
            @RequestParam(required = false) String tipoSolicitacao,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String prepostoEmpresa,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") Sort.Direction sortOrder) {
        SolicitacaoRequest payload = new SolicitacaoRequest(null, nomeEmpresa, cnpj, endereco, telefone, tipoSolicitacao, email, prepostoEmpresa);
        var pageable = PageRequest.of(page, size, Sort.by(sortOrder, sortBy));
        var result = this.service.findAllByFilters(payload, pageable);
        return okResponseEntity(result);
    }

    @GetMapping("/{id}")
    public BaseResponse<SolicitacaoResponse> findById(@PathVariable Long id) {
        var result = this.service.findById(id);
        return okSuccess(result);
    }

    @PatchMapping("/analista/finaliza/{id}")
    public BaseResponse<SolicitacaoResponse> analistaFinalizaSolicitacao(@PathVariable Long id,
                                                                         @RequestBody @Valid AnalistaFinalizaSolicitacaoRequest payload) {
        var result = this.service.analistaFinalizaSolicitacaoRequest(id, payload);
        return okSuccess(result);
    }

    @PatchMapping("/conselho-diretor/finaliza/{id}")
    public BaseResponse<SolicitacaoResponse> conselhoDiretorFinalizaSolicitacao(@PathVariable Long id,
                                                                                @RequestBody @Valid ConselhoDiretorFinalizaSolicitacaoRequest payload) {
        var result = this.service.conselhorDiretorFinalizaSolicitacaoRequest(id, payload);
        return okSuccess(result);
    }

    @GetMapping("/dashboard")
    public BaseResponse<List<DashboardResponse>> dashboard() {
        var result = this.service.dashboard();
        return okSuccess(result);
    }



}
