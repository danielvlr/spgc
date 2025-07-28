package br.gov.ce.arce.spgc.repository;

import br.gov.ce.arce.spgc.model.dto.DashboardResponse;
import br.gov.ce.arce.spgc.model.entity.Solicitacao;
import br.gov.ce.arce.spgc.model.enumeration.SolicitacaoStatus;
import br.gov.ce.arce.spgc.model.enumeration.TipoSolicitacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SolicitacaoRepository extends JpaRepository<Solicitacao, Long> {
    List<Solicitacao> findByCnpj(String cnpj);
    Optional<Solicitacao> findByCnpjAndTipoSolicitacaoAndStatusIn(String cnpj, TipoSolicitacao tipoSolicitacao, List<SolicitacaoStatus> status);

    @Query("""
       SELECT new br.gov.ce.arce.spgc.model.dto.DashboardResponse(
           s.tipoSolicitacao, COUNT(s)
       )
       FROM Solicitacao s
       WHERE s.status IN :statusList
       GROUP BY s.tipoSolicitacao
       """)
    List<DashboardResponse> dashboard(@Param("statusList") List<SolicitacaoStatus> statusList);
}

