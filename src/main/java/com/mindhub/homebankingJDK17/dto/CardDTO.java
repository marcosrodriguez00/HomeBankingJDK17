package com.mindhub.homebankingJDK17.dto;

import com.mindhub.homebankingJDK17.models.Card;
import com.mindhub.homebankingJDK17.models.CardColor;
import com.mindhub.homebankingJDK17.models.CardType;

import java.time.LocalDate;

public class CardDTO {

    private long id;

    private String cardholder, number, cvv;

    private CardType type;

    private CardColor color;

    private LocalDate thruDate, fromDate;

    private boolean isActive;

    public CardDTO( Card card ) {
        id = card.getId();
        cardholder = card.getCardholder();
        number = card.getNumber();
        cvv = card.getCvv();
        type = card.getType();
        color = card.getColor();
        thruDate = card.getThruDate();
        fromDate = card.getFromDate();
        isActive = card.isActive();
    }

    public long getId() {
        return id;
    }

    public String getCardholder() {
        return cardholder;
    }

    public String getNumber() {
        return number;
    }

    public String getCvv() {
        return cvv;
    }

    public CardType getType() {
        return type;
    }

    public CardColor getColor() {
        return color;
    }

    public LocalDate getThruDate() {
        return thruDate;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public boolean isActive() {
        return isActive;
    }
}
