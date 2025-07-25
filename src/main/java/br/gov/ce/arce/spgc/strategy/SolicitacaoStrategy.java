package br.gov.ce.arce.spgc.strategy;

import br.gov.ce.arce.spgc.exception.BusinessException;
import br.gov.ce.arce.spgc.model.entity.Solicitacao;
import br.gov.ce.arce.spgc.model.enumeration.TipoDocumento;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;

import java.util.*;
import java.util.stream.Collectors;

public interface SolicitacaoStrategy {

    void valida(Solicitacao solicitacao);

    enum RegraTipo {PELO_MENOS_UM, TODOS}

    record Regra(String grupo, List<Integer> codigos, RegraTipo tipo, String descricao) {
    }

    default void validaRegras(Solicitacao solicitacao, List<Regra> regras) {
        Set<Integer> codigosPresentes = solicitacao.getArquivos().stream()
                .map(a -> a.getTipoDocumento().getCodigo())
                .collect(Collectors.toSet());

        // Montar a lista com os grupos inválidos
        List<Map<String, Object>> gruposInvalidos = regras.stream()
                .filter(regra -> !isRegraValida(regra, codigosPresentes))
                .map(regra -> montarJsonRegra(regra, codigosPresentes))
                .toList();

        if (!gruposInvalidos.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("statusCode", HttpStatus.BAD_REQUEST.value());
            response.put("mensagem", "Não foi possível criar a solicitação. Faltam documentos obrigatórios.");
            response.put("regras", gruposInvalidos);

            throw new BusinessException(response.toString(), HttpStatus.BAD_REQUEST);
        }
    }

    private boolean isRegraValida(Regra regra, Set<Integer> codigosPresentes) {
        return switch (regra.tipo()) {
            case PELO_MENOS_UM -> regra.codigos().stream().anyMatch(codigosPresentes::contains);
            case TODOS -> codigosPresentes.containsAll(regra.codigos());
        };
    }

    private Map<String, Object> montarJsonRegra(Regra regra, Set<Integer> codigosPresentes) {
        List<Map<String, String>> documentosFaltantes = regra.codigos().stream()
                .filter(codigo -> !codigosPresentes.contains(codigo))
                .map(codigo -> Map.of(
                        "codigo", String.valueOf(codigo),
                        "descricao", TipoDocumento.fromCodigo(codigo).getDescricao()
                ))
                .toList();

        Map<String, Object> json = new LinkedHashMap<>();
        json.put("grupo", regra.grupo());
        json.put("descricao", regra.descricao());
        json.put("documentosFaltantes", documentosFaltantes);

        return json;
    }
}
