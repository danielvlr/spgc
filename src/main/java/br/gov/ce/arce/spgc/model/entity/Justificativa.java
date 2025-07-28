package br.gov.ce.arce.spgc.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.envers.Audited;

@Data
@Entity
@Audited
@Table(schema = "spgc", name = "justificativa")
public class Justificativa {
    public static final String SEQUENCE = "spgc" + ".justificativa_id_seq";

    @Id
    @SequenceGenerator(name = SEQUENCE, sequenceName = SEQUENCE, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String descricao;
}
