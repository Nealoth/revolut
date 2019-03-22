package com.sometest.service;

import com.sometest.dto.BankAccountDTO;
import com.sometest.dto.ClientDTO;
import com.sometest.exception.EntityNotFoundException;
import com.sometest.model.entity.BankAccount;
import com.sometest.model.entity.Client;
import com.sometest.repository.ClientRepository;
import com.sometest.util.ValidationUtils;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class ClientService {

	private final ClientRepository clientRepository;

	public List<ClientDTO> getClients() {
		return clientRepository
				.getClients()
				.stream()
				.map(Client::toDTO)
				.collect(Collectors.toList());
	}

	public ClientDTO createClient(ClientDTO clientDTO) {
		ValidationUtils.validate(clientDTO);

		Client client = new Client(clientDTO);

		return clientRepository
				.createClient(client)
				.toDTO();
	}

	public ClientDTO getById(Long clientId) {
		checkNotNull(clientId);

		return Optional
				.ofNullable(clientRepository.getById(clientId))
				.map(Client::toDTO)
				.orElseThrow(() -> new EntityNotFoundException("Client not found"));
	}

	public ClientDTO delete(Long clientId) {
		checkNotNull(clientId);

		return Optional
				.ofNullable(clientRepository.deleteClient(clientId))
				.map(Client::toDTO)
				.orElseThrow(() -> new EntityNotFoundException("Client not found"));
	}

	public List<BankAccountDTO> getClientAcocunts(Long clientId) {
		checkNotNull(clientId);

		return Optional
				.ofNullable(clientRepository.getById(clientId))
				.map(Client::getBankAccounts)
				.orElseThrow(() -> new EntityNotFoundException("Client not found"))
				.stream()
				.map(BankAccount::toDTO)
				.collect(Collectors.toList());
	}

}
