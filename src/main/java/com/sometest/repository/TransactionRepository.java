package com.sometest.repository;

import com.sometest.model.entity.Transaction;

public interface TransactionRepository {

	Transaction create(Transaction transaction);

	Transaction update(Transaction transaction);

}
