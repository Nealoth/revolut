package com.sometest.repository.impl;

import com.sometest.model.entity.BankAccount;
import com.sometest.repository.BankAccountRepository;

import javax.inject.Singleton;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Singleton
class InMemoryBankAccountRepository implements BankAccountRepository {

	private final static Map<Long, BankAccount> store = new ConcurrentHashMap<>();
	private AtomicLong id_sequence = new AtomicLong(0L);

	@Override
	public List<BankAccount> getAllAccounts() {
		return new ArrayList<>(store.values());
	}

	@Override
	public BankAccount createBankAccount(BankAccount bankAccount) {
		bankAccount.setId(id_sequence.incrementAndGet());
		store.put(bankAccount.getId(), bankAccount);
		return bankAccount;
	}

	@Override
	public BankAccount getById(Long id) {
		return store.get(id);
	}

	@Override
	public BankAccount update(BankAccount bankAccount) {
		store.put(bankAccount.getId(), bankAccount);
		return bankAccount;
	}

	@Override
	public void makeTransfer(BankAccount from, BankAccount to, BigDecimal amount) {
		from.setBalance(from.getBalance().subtract(amount));
		store.put(from.getId(), from);

		to.setBalance(to.getBalance().add(amount));
		store.put(to.getId(), to);
	}


}
