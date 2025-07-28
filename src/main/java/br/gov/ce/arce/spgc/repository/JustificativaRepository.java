package br.gov.ce.arce.spgc.repository;

import br.gov.ce.arce.spgc.model.entity.Arquivo;
import br.gov.ce.arce.spgc.model.entity.Justificativa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JustificativaRepository extends JpaRepository<Justificativa, Long> {
    boolean existsByDescricaoIgnoreCase(String descricao);
}

