package ru.jekajops.quadcopterbot.service;

import ru.jekajops.quadcopterbot.models.Client;
import ru.jekajops.quadcopterbot.repository.ClientRepository;

public class ClientService implements DataService<Client, Long, ClientRepository> {
    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public ClientRepository repository() {
        return clientRepository;
    }
}
