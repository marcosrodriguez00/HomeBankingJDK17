package com.mindhub.homebankingJDK17.services.implement;

import com.mindhub.homebankingJDK17.dto.ClientDTO;
import com.mindhub.homebankingJDK17.models.Client;
import com.mindhub.homebankingJDK17.repositories.ClientRepository;
import com.mindhub.homebankingJDK17.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class ClientImplement implements ClientService {
    @Autowired
    ClientRepository clientRepository;

    @Override
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @Override
    public List<ClientDTO> getAllClientDTO() {
        return getAllClients().stream().map(ClientDTO::new).collect(toList());
    }

    @Override
    public Client getClientById(Long id) {
        return clientRepository.findById(id).orElse(null);
    }

    @Override
    public ClientDTO getClientDTOById(Long id) {
        return new ClientDTO(getClientById(id));
    }

    @Override
    public Client getClientByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

    @Override
    public void saveClient(Client client) {
        clientRepository.save(client);
    }
}
