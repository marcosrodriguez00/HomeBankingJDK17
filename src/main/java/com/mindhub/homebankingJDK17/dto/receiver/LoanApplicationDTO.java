package com.mindhub.homebankingJDK17.dto.receiver;

public record LoanApplicationDTO(Long loanId, int payments, Double amount, Double interestRate, String destinyAccountNumber) {
}
