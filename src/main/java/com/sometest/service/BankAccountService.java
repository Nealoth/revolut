package com.sometest.service;

import com.google.common.base.Preconditions;
import com.sometest.dto.BankAccountDTO;
import com.sometest.exception.EntityNotFoundException;
import com.sometest.model.entity.BankAccount;
import com.sometest.model.entity.Client;
import com.sometest.repository.BankAccountRepository;
import com.sometest.repository.ClientRepository;
import com.sometest.util.ValidationUtils;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class BankAccountService {

	private final BankAccountRepository bankAccountRepository;
	private final ClientRepository clientRepository;

	public BankAccountDTO createBankAccount(BankAccountDTO bankAccountDTO) {
		ValidationUtils.validate(bankAccountDTO);

		Client client = clientRepository.getById(bankAccountDTO.getClientId());

		if (client == null) {
			throw new EntityNotFoundException("Client not found");
		}

		BankAccount bankAccount = new BankAccount(bankAccountDTO, client);
		BankAccount createdBankAccount = bankAccountRepository.createBankAccount(bankAccount);
		client.getBankAccounts().add(createdBankAccount);

		return createdBankAccount.toDTO();
	}

	public List<BankAccountDTO> getBankAccounts() {
		return bankAccountRepository
				.getAllAccounts()
				.stream()
				.map(BankAccount::toDTO)
				.collect(toList());
	}

	public BankAccountDTO getBankAccountById(Long bankAccountId) {
		Preconditions.checkNotNull(bankAccountId);
		return Optional
				.ofNullable(bankAccountRepository.getById(bankAccountId))
				.map(BankAccount::toDTO)
				.orElseThrow(() -> new EntityNotFoundException("Bank account not found"));
	}

}
