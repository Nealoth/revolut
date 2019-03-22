package com.sometest.repository;

import com.sometest.model.entity.Client;

import java.util.List;

public interface ClientRepository {

	List<Client> getClients();

	Client createClient(Client client);

	Client deleteClient(Long clientId);

	Client getById(Long clientId);

}
