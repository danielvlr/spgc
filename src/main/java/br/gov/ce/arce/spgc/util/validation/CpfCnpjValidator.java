package br.gov.ce.arce.spgc.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CpfCnpjValidator implements ConstraintValidator<CpfCnpj, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true; // considere usar @NotBlank separado
        }

        String digits = value.replaceAll("\\D", ""); // remove pontuação

        return isValidCPF(digits) || isValidCNPJ(digits);
    }

    private boolean isValidCPF(String cpf) {
        if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) return false;

        try {
            int sum1 = 0, sum2 = 0;
            for (int i = 0; i < 9; i++) {
                int digit = Character.getNumericValue(cpf.charAt(i));
                sum1 += digit * (10 - i);
                sum2 += digit * (11 - i);
            }

            int d1 = sum1 % 11;
            d1 = d1 < 2 ? 0 : 11 - d1;

            sum2 += d1 * 2;
            int d2 = sum2 % 11;
            d2 = d2 < 2 ? 0 : 11 - d2;

            return cpf.charAt(9) == Character.forDigit(d1, 10) &&
                    cpf.charAt(10) == Character.forDigit(d2, 10);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isValidCNPJ(String cnpj) {
        if (cnpj.length() != 14 || cnpj.matches("(\\d)\\1{13}")) return false;

        try {
            int[] weights1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
            int[] weights2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

            int sum1 = 0, sum2 = 0;
            for (int i = 0; i < 12; i++) {
                int digit = Character.getNumericValue(cnpj.charAt(i));
                sum1 += digit * weights1[i];
                sum2 += digit * weights2[i];
            }

            int d1 = sum1 % 11;
            d1 = d1 < 2 ? 0 : 11 - d1;

            sum2 += d1 * weights2[12];
            int d2 = sum2 % 11;
            d2 = d2 < 2 ? 0 : 11 - d2;

            return cnpj.charAt(12) == Character.forDigit(d1, 10) &&
                    cnpj.charAt(13) == Character.forDigit(d2, 10);
        } catch (Exception e) {
            return false;
        }
    }
}
