package com.sometest.repository;

import com.sometest.model.entity.BankAccount;

import java.math.BigDecimal;
import java.util.List;

public interface BankAccountRepository {

	List<BankAccount> getAllAccounts();

	BankAccount createBankAccount(BankAccount bankAccount);

	BankAccount getById(Long id);

	BankAccount update(BankAccount bankAccount);

	void makeTransfer(BankAccount from, BankAccount to, BigDecimal amount);

}
