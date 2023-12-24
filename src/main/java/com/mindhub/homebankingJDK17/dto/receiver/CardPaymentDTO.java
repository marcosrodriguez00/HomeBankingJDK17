package com.mindhub.homebankingJDK17.dto.receiver;

public record CardPaymentDTO(String cardNumber, String paymentDescription, Double paymentAmount) {
}