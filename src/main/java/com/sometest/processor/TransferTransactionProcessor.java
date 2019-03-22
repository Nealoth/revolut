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
class TransferTransactionProcessor implements TransactionProcessor {
	private final TransactionRepository transactionRepository;
	private final BankAccountRepository bankAccountRepository;

	@Override
	public void process(Transaction transaction) {
		transaction.setStatus(IN_PROGRESS);
		transactionRepository.update(transaction);

		BankAccount sender = transaction.getFrom();
		BankAccount addressee = transaction.getTo();

		if (sender.getBalance().compareTo(transaction.getAmount()) >= 0) {
			bankAccountRepository.makeTransfer(sender, addressee, transaction.getAmount());

			transaction.setStatus(SUCCESS);
			transactionRepository.update(transaction);
		} else {
			transaction.setStatus(FAILED);
			transactionRepository.update(transaction);
			throw new TransactionFailException("Client does not have enough funds");
		}
	}
}
