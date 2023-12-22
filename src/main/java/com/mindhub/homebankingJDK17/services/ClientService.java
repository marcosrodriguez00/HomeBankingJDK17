package com.mindhub.homebankingJDK17.services;

import com.mindhub.homebankingJDK17.dto.ClientDTO;
import com.mindhub.homebankingJDK17.models.Client;

import java.util.List;

public interface ClientService {
    List<Client> getAllClients();

    List<ClientDTO> getAllClientDTO();

    Client getClientById(Long id);

    ClientDTO getClientDTOById(Long id);

    Client getClientByEmail(String email);

    void saveClient(Client client);
}