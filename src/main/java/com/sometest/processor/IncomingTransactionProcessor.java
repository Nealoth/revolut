package com.sometest.processor;

import com.sometest.model.entity.BankAccount;
import com.sometest.model.entity.Transaction;
import com.sometest.repository.BankAccountRepository;
import com.sometest.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.sometest.model.TransactionStatus.IN_PROGRESS;
import static com.sometest.model.TransactionStatus.SUCCESS;

@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
class IncomingTransactionProcessor implements TransactionProcessor {

	private final TransactionRepository transactionRepository;
	private final BankAccountRepository bankAccountRepository;

	@Override
	public void process(Transaction transaction) {
		transaction.setStatus(IN_PROGRESS);
		transactionRepository.update(transaction);

		BankAccount addresseeAccount = transaction.getTo();
		addresseeAccount.setBalance(addresseeAccount.getBalance().add(transaction.getAmount()));
		bankAccountRepository.update(addresseeAccount);

		transaction.setStatus(SUCCESS);
		transactionRepository.update(transaction);
	}

}
