package com.sometest.repository.impl;

import com.sometest.model.entity.Transaction;
import com.sometest.repository.TransactionRepository;

import javax.inject.Singleton;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Singleton
class InMemoryTransactionRepository implements TransactionRepository {

	private final static Map<Long, Transaction> store = new ConcurrentHashMap<>();
	private AtomicLong id_sequence = new AtomicLong(0L);


	@Override
	public Transaction create(Transaction transaction) {
		transaction.setId(id_sequence.incrementAndGet());
		store.put(transaction.getId(), transaction);
		return transaction;
	}

	@Override
	public Transaction update(Transaction transaction) {
		store.put(transaction.getId(), transaction);
		return transaction;
	}
}
