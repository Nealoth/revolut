package com.sometest.processor;

import com.sometest.model.entity.Transaction;

public interface TransactionProcessor {
	public void process(Transaction transaction);
}
