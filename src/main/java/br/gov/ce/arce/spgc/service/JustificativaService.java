package br.gov.ce.arce.spgc.service;

import br.gov.ce.arce.spgc.exception.BusinessException;
import br.gov.ce.arce.spgc.model.BasePageResponse;
import br.gov.ce.arce.spgc.model.dto.CreateJustificativaRequest;
import br.gov.ce.arce.spgc.model.dto.JustificativaRequest;
import br.gov.ce.arce.spgc.model.dto.JustificativaResponse;
import br.gov.ce.arce.spgc.model.entity.Justificativa;
import br.gov.ce.arce.spgc.model.mapper.JustificativaMapper;
import br.gov.ce.arce.spgc.repository.JustificativaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JustificativaService {

    private final JustificativaRepository repository;
    private final JustificativaMapper mapper;

    @Transactional
    public JustificativaResponse create(CreateJustificativaRequest request) {
        if (repository.existsByDescricaoIgnoreCase(request.descricao())) {
            throw BusinessException.createBadRequestBusinessException("Já existe uma justificativa com esta descrição");
        }

        Justificativa entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional(readOnly = true)
    public JustificativaResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> BusinessException.createNotFoundBusinessException("Justificativa não encontrada"));
    }

    @Transactional(readOnly = true)
    public BasePageResponse<JustificativaResponse> findAllByFilters(JustificativaRequest payload, PageRequest pageable) {
        var justificativa = mapper.toEntity(payload);
        var matcher = ExampleMatcher.matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        var result = repository.findAll(Example.of(justificativa, matcher), pageable)
                .map(mapper::toResponse);

        return BasePageResponse.createBySpringPage(result);
    }

    @Transactional
    public JustificativaResponse update(Long id, JustificativaRequest request) {
        Justificativa entity = repository.findById(id)
                .orElseThrow(() -> BusinessException.createNotFoundBusinessException("Justificativa não encontrada"));

        entity.setDescricao(request.descricao());
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw BusinessException.createNotFoundBusinessException("Justificativa não encontrada");
        }
        repository.deleteById(id);
    }
}
