package com.sometest.repository.impl;

import com.sometest.model.entity.Client;
import com.sometest.repository.ClientRepository;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Singleton
class InMemoryClientRepository implements ClientRepository {

	private final static Map<Long, Client> store = new ConcurrentHashMap<>();
	private AtomicLong id_sequence = new AtomicLong(0L);

	@Override
	public List<Client> getClients() {
		return new ArrayList<>(store.values());
	}

	@Override
	public Client createClient(Client client) {
		client.setId(id_sequence.incrementAndGet());
		store.put(client.getId(), client);
		return client;
	}

	@Override
	public Client deleteClient(Long clientId) {
		Client client = store.get(clientId);
		store.remove(clientId);
		return client;
	}

	@Override
	public Client getById(Long clientId) {
		return store.get(clientId);
	}
}
