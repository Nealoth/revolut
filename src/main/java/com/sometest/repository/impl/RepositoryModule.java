package com.sometest.repository.impl;

import com.google.inject.AbstractModule;
import com.sometest.repository.BankAccountRepository;
import com.sometest.repository.ClientRepository;
import com.sometest.repository.TransactionRepository;

public class RepositoryModule extends AbstractModule {
	@Override
	protected void configure() {
		bind(ClientRepository.class).to(InMemoryClientRepository.class);
		bind(BankAccountRepository.class).to(InMemoryBankAccountRepository.class);
		bind(TransactionRepository.class).to(InMemoryTransactionRepository.class);
	}
}
