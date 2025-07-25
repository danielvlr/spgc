package br.gov.ce.arce.spgc.model.entity;

import br.gov.ce.arce.spgc.model.enumeration.SolicitacaoStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "Necessario informar o nome da empresa.")
    private String nomeEmpresa;

    @CNPJ(message = "CNPJ inválido")
    @NotNull(message = "Necessario informar o cnpj da empresa.")
    private String cnpj;

    @NotNull(message = "Necessario informar o endereco da empresa.")
    private String endereco;

    @NotNull(message = "Necessario informar o telefone da empresa.")
    private String telefone;

    @NotNull(message = "Necessario informar o tipo da solicitação.")
    private String tipoSolicitacao;

    @Email
    @NotNull(message = "Necessario informar o email da empresa.")
    private String email;

    @NotNull(message = "Necessario informar o preposto da empresa.")
    private String prepostoEmpresa;

    private String token;

    private String nupSuite;

    @NotNull(message = "Necessario informar o status da solicitação.")
    private SolicitacaoStatus status;

    private String justificativa;

    @OneToMany(mappedBy = "solicitacao", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Arquivo> arquivos;

}
