package br.gov.ce.arce.spgc.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreateSolicitacaoRequest(
        @NotBlank(message = "O nome da empresa é obrigatório")
        String nomeEmpresa,

        @NotBlank(message = "O CNPJ é obrigatório")
        String cnpj,

        @NotBlank(message = "O endereço é obrigatório")
        String endereco,

        @NotBlank(message = "O telefone é obrigatório")
        String telefone,

        @NotBlank(message = "O tipo da solicitacao é obrigatório")
        String tipoSolicitacao,

        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "E-mail inválido")
        String email,

        @NotBlank(message = "O preposto da empresa é obrigatório")
        String prepostoEmpresa,

        @NotEmpty(message = "A lista de arquivos não pode estar vazia")
        @Valid
        List<CreateSolicitacaoArquivoRequest> arquivos
) {}