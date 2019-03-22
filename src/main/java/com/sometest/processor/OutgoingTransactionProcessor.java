package com.sometest.processor;

import com.sometest.exception.TransactionFailException;
import com.sometest.model.entity.BankAccount;
import com.sometest.model.entity.Transaction;
import com.sometest.repository.BankAccountRepository;
import com.sometest.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.sometest.model.TransactionStatus.*;

@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
class OutgoingTransactionProcessor implements TransactionProcessor {
	private final TransactionRepository transactionRepository;
	private final BankAccountRepository bankAccountRepository;

	@Override
	public void process(Transaction transaction) {
		transaction.setStatus(IN_PROGRESS);
		transactionRepository.update(transaction);

		BankAccount clientAccount = transaction.getFrom();

		if (clientAccount.getBalance().compareTo(transaction.getAmount()) >= 0) {
			clientAccount.setBalance(clientAccount.getBalance().subtract(transaction.getAmount()));
			bankAccountRepository.update(clientAccount);

			transaction.setStatus(SUCCESS);
			transactionRepository.update(transaction);
		} else {
			transaction.setStatus(FAILED);
			transactionRepository.update(transaction);
			throw new TransactionFailException("Client does not have enough funds");
		}
	}
}
