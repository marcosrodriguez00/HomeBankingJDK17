package com.mindhub.homebankingJDK17.services;

import com.mindhub.homebankingJDK17.dto.CardDTO;
import com.mindhub.homebankingJDK17.models.Card;
import com.mindhub.homebankingJDK17.models.CardColor;
import com.mindhub.homebankingJDK17.models.CardType;
import com.mindhub.homebankingJDK17.models.Client;

import java.util.List;

public interface CardService {
    Card getCardById(Long id);

    CardDTO getCardDTOById(Long id);

    List<CardDTO> getAllCardDTO();

    List<Card> getAllCards();

    Boolean existsCardByTypeAndColorAndClientAndIsActive(CardType cardType, CardColor cardColor, Client client, boolean isActive);

    Boolean existsCardByNumber(String cardNumber);

    void saveCard(Card card);

    Card getCardByNumber(String number);

    void deleteCardByNumber(String number);
}
