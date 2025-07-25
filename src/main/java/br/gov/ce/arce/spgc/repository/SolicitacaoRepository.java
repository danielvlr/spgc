package br.gov.ce.arce.spgc.repository;

import br.gov.ce.arce.spgc.model.entity.Solicitacao;
import br.gov.ce.arce.spgc.model.enumeration.SolicitacaoStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SolicitacaoRepository extends JpaRepository<Solicitacao, Long> {
    List<Solicitacao> findByCnpj(String cnpj);
    Optional<Solicitacao> findByCnpjAndTipoSolicitacaoAndStatus(String cnpj, String tipoSolicitacao, Boolean status);

    Optional<Solicitacao> findByIdAndToken(Long id, String token);
    Optional<Solicitacao> findFirstByCnpjAndTipoSolicitacaoAndStatusIn(String cnpj, String tipoSolicitacao, List<SolicitacaoStatus> status);
}

