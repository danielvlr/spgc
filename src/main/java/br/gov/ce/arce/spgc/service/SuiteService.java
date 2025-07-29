package br.gov.ce.arce.spgc.service;

import br.gov.ce.arce.spgc.client.suite.SuiteClient;
import br.gov.ce.arce.spgc.exception.BusinessException;
import br.gov.ce.arce.spgc.model.dto.suite.CriarProcessoExternoRequest;
import br.gov.ce.arce.spgc.model.dto.suite.CriarProcessoExternoResponse;
import feign.FeignException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SuiteService {

    private final SuiteClient suiteClient;

    public CriarProcessoExternoResponse criarProcessoSistemaExterno(
            @NotNull @Valid CriarProcessoExternoRequest request) {
        try {
            var response = suiteClient.criarProcessoExterno(request);

            if (response == null) {
                throw new BusinessException("Consulta ao SUITE não retornou dados.");
            }

            return response.getBody();

        } catch (FeignException.NotFound e) {
            throw BusinessException.createNotFoundBusinessException("Registro não encontrado no SUITE.");
        } catch (FeignException.BadRequest e) {
            throw BusinessException.createBadRequestBusinessException("Consulta ao SUITE não pode ser processada. Verifique os dados da consulta e tente novamente.");
        } catch (FeignException e) {
            String statusCode = String.valueOf(e.status());
            throw BusinessException.createWithStatusBusinessException("Erro ao chamar serviço SUITE.", statusCode);
        } catch (Exception e) {
            throw new BusinessException("Consulta ao SUITE não pode ser processada.");
        }
    }

}
