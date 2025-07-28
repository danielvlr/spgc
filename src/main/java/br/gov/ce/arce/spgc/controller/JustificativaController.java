package br.gov.ce.arce.spgc.controller;

import br.gov.ce.arce.spgc.model.BasePageResponse;
import br.gov.ce.arce.spgc.model.BaseResponse;
import br.gov.ce.arce.spgc.model.dto.CreateJustificativaRequest;
import br.gov.ce.arce.spgc.model.dto.JustificativaRequest;
import br.gov.ce.arce.spgc.model.dto.JustificativaResponse;
import br.gov.ce.arce.spgc.service.JustificativaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/justificativa")
@RequiredArgsConstructor
public class JustificativaController extends BaseController{

    private final JustificativaService service;

    @PostMapping
    public ResponseEntity<JustificativaResponse> create(@RequestBody CreateJustificativaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<JustificativaResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    public ResponseEntity<BaseResponse<BasePageResponse<JustificativaResponse>>> findAll(
            @RequestParam(required = false) String descricao,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") Sort.Direction sortOrder) {

        JustificativaRequest payload = new JustificativaRequest(null,descricao);
        var pageable = PageRequest.of(page, size, Sort.by(sortOrder, sortBy));
        var result = this.service.findAllByFilters(payload, pageable);
        return okResponseEntity(result);
    }


    @PutMapping("/{id}")
    public ResponseEntity<JustificativaResponse> update(@PathVariable Long id, @RequestBody JustificativaRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
