package br.gov.ce.arce.spgc.model.entity;

import br.gov.ce.arce.spgc.model.enumeration.SolicitacaoStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.br.CNPJ;

import java.util.List;


@Data
@Entity
@Audited
@Table(schema = "spgc", name = "solicitacao")
public class Solicitacao extends BaseEntity{
    public static final String SEQUENCE = "spgc" + ".solicitacao_id_seq";

    @Id
    @SequenceGenerator(name = SEQUENCE, sequenceName = SEQUENCE, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE)
    private Long id;

    private String nomeEmpresa;

    @CNPJ(message = "CNPJ inválido")
    private String cnpj;

    private String endereco;

    private String telefone;

    private String tipoSolicitacao;

    @Email
    private String email;

    private String prepostoEmpresa;

    private SolicitacaoStatus status;

    @OneToMany(mappedBy = "solicitacao", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Arquivo> arquivos;
}
