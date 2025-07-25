package br.gov.ce.arce.spgc.controller;

import br.gov.ce.arce.spgc.model.BasePageResponse;
import br.gov.ce.arce.spgc.model.BaseResponse;
import br.gov.ce.arce.spgc.model.dto.SolicitacaoRequest;
import br.gov.ce.arce.spgc.model.dto.SolicitacaoResponse;
import br.gov.ce.arce.spgc.service.SolicitacaoService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static br.gov.ce.arce.spgc.model.BaseResponse.okSuccess;

@RestController
@RequestMapping("solicitacao")
@AllArgsConstructor
public class Solicitacao extends BaseController {

    private final SolicitacaoService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
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
    public BaseResponse<SolicitacaoResponse> findById(@PathVariable(required = true) Long id) {
        var result = this.service.findById(id);
        return okSuccess(result);
    }
}
