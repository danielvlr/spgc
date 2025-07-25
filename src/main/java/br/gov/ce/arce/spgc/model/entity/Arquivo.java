package br.gov.ce.arce.spgc.model.entity;

import br.gov.ce.arce.spgc.model.enumeration.TipoDocumento;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.envers.Audited;


@Data
@Entity
@Audited
@Table(schema = "spgc", name = "arquivo")
public class Arquivo extends BaseEntity{
    public static final String SEQUENCE = "spgc" + ".arquivo_id_seq";

    @Id
    @SequenceGenerator(name = SEQUENCE, sequenceName = SEQUENCE, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TipoDocumento tipoDocumento;

    @Column(length = 4000)
    private String url;
    private Boolean valido;
    private String justificativa;

    @ManyToOne
    private Solicitacao solicitacao;

    @Transient
    private String conteudoBase64;
}
