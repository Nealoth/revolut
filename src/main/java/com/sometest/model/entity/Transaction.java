package com.sometest.model.entity;

import com.sometest.dto.Convertable;
import com.sometest.dto.TransactionDTO;
import com.sometest.model.TransactionStatus;
import com.sometest.model.TransactionType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class Transaction implements Convertable<TransactionDTO> {
	private Long id;
	private BankAccount from;
	private BankAccount to;
	private BigDecimal amount;
	private TransactionType type;
	private TransactionStatus status;

	public Transaction(TransactionDTO dto, BankAccount from, BankAccount to) {
		this.from = from;
		this.to = to;
		this.amount = dto.getAmount();
		this.type = dto.getType();
		this.status = dto.getStatus();
	}

	@Override
	public TransactionDTO toDTO() {
		return TransactionDTO.builder()
				.id(this.id)
				.from(this.from.getId())
				.to(this.to.getId())
				.amount(this.amount)
				.type(this.type)
				.status(this.status)
				.build();
	}
}
