package com.sometest.service;

import com.sometest.dto.TransactionDTO;
import com.sometest.exception.EntityNotFoundException;
import com.sometest.model.entity.BankAccount;
import com.sometest.model.entity.Transaction;
import com.sometest.processor.TransactionProcessor;
import com.sometest.processor.TransactionProcessorFactory;
import com.sometest.repository.BankAccountRepository;
import com.sometest.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

import static com.sometest.model.TransactionStatus.NOT_STARTED;
import static com.sometest.model.TransactionType.*;
import static com.sometest.util.ValidationUtils.validateNotNull;

@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class TransactionService {

	private final TransactionRepository transactionRepository;
	private final TransactionProcessorFactory transactionProcessorFactory;
	private final BankAccountRepository bankAccountRepository;

	public void income(TransactionDTO transactionDTO) {
		validateNotNull(transactionDTO.getTo());
		transactionDTO.setType(INCOMING);
		transactionDTO.setStatus(NOT_STARTED);

		BankAccount addressee = Optional
				.ofNullable(bankAccountRepository.getById(transactionDTO.getTo()))
				.orElseThrow(() -> new EntityNotFoundException("Addressee account not found"));

		Transaction transaction = new Transaction(transactionDTO, null, addressee);
		Transaction createdTransaction = transactionRepository.create(transaction);

		process(createdTransaction);
	}

	public void withdraw(TransactionDTO transactionDTO) {
		validateNotNull(transactionDTO.getFrom());
		transactionDTO.setType(OUTGOING);
		transactionDTO.setStatus(NOT_STARTED);

		BankAccount sender = Optional
				.ofNullable(bankAccountRepository.getById(transactionDTO.getFrom()))
				.orElseThrow(() -> new EntityNotFoundException("Sender account not found"));

		Transaction transaction = new Transaction(transactionDTO, sender, null);
		Transaction createdTransaction = transactionRepository.create(transaction);

		process(createdTransaction);
	}

	public void transfer(TransactionDTO transactionDTO) {
		validateNotNull(transactionDTO.getTo(), transactionDTO.getFrom());
		transactionDTO.setType(BETWEEEN_ACCOUNTS);
		transactionDTO.setStatus(NOT_STARTED);

		BankAccount sender = Optional
				.ofNullable(bankAccountRepository.getById(transactionDTO.getFrom()))
				.orElseThrow(() -> new EntityNotFoundException("Sender account not found"));

		BankAccount addressee = Optional
				.ofNullable(bankAccountRepository.getById(transactionDTO.getTo()))
				.orElseThrow(() -> new EntityNotFoundException("Addressee account not found"));

		Transaction transaction = new Transaction(transactionDTO, sender, addressee);
		Transaction createdTransaction = transactionRepository.create(transaction);

		process(createdTransaction);
	}

	private void process(Transaction transaction) {
		TransactionProcessor processor = transactionProcessorFactory.getInstance(transaction.getType());
		processor.process(transaction);
	}


}
