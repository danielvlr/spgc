package br.gov.ce.arce.spgc.repository;

import br.gov.ce.arce.spgc.model.entity.Arquivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolicitacaoArquivoRepository extends JpaRepository<Arquivo, Long> {
}

