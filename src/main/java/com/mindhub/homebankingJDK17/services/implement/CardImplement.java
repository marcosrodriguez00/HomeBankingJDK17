package com.mindhub.homebankingJDK17.services.implement;

import com.mindhub.homebankingJDK17.dto.CardDTO;
import com.mindhub.homebankingJDK17.models.Card;
import com.mindhub.homebankingJDK17.models.CardColor;
import com.mindhub.homebankingJDK17.models.CardType;
import com.mindhub.homebankingJDK17.models.Client;
import com.mindhub.homebankingJDK17.repositories.CardRepository;
import com.mindhub.homebankingJDK17.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class CardImplement implements CardService {
    @Autowired
    CardRepository cardRepository;

    @Override
    public Card getCardById(Long id) {
        return cardRepository.findById(id).orElse(null);
    }

    @Override
    public CardDTO getCardDTOById(Long id) {
        return new CardDTO(getCardById(id));
    }

    @Override
    public List<Card> getAllCards() {
        return cardRepository.findAll();
    }

    @Override
    public List<CardDTO> getAllCardDTO() {
        return getAllCards().stream().map(CardDTO::new).collect(toList());
    }

    @Override
    public Boolean existsCardByTypeAndColorAndClientAndIsActive(CardType cardType, CardColor cardColor, Client client, boolean isActive) {
        return cardRepository.existsByTypeAndColorAndClientAndIsActive(cardType, cardColor, client, isActive);
    }

    @Override
    public Boolean existsCardByNumber(String cardNumber) {
        return cardRepository.existsByNumber(cardNumber);
    }

    @Override
    public void saveCard(Card card) {
        cardRepository.save(card);
    }

    @Override
    public Card getCardByNumber(String number) {
        return cardRepository.findByNumber(number);
    }

    @Override
    public void deleteCardByNumber(String number) {
        Card card = getCardByNumber(number);
        card.setActive(false);
        saveCard(card);
    }
}
