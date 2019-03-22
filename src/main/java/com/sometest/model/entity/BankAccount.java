package com.sometest.model.entity;

import com.sometest.dto.BankAccountDTO;
import com.sometest.dto.Convertable;
import com.sometest.model.Currency;
import lombok.Data;

import java.math.BigDecimal;

import static com.sometest.model.Currency.RUB;
import static java.math.BigDecimal.ROUND_HALF_EVEN;
import static java.math.BigDecimal.ZERO;
import static java.util.Optional.ofNullable;

@Data
public class BankAccount implements Convertable<BankAccountDTO> {

	private Long id;
	private Client client;
	private BigDecimal balance = ZERO.setScale(2, ROUND_HALF_EVEN);
	private boolean blocked = false;
	private Currency currency = RUB;

	public BankAccount(BankAccountDTO dto, Client client) {
		this.balance = ofNullable(dto.getBalance()).orElse(this.balance);
		this.blocked = dto.isBlocked();
		this.currency = ofNullable(dto.getCurrency()).orElse(this.currency);
		this.client = client;
	}

	@Override
	public BankAccountDTO toDTO() {
		return BankAccountDTO.builder()
				.id(this.id)
				.clientId(this.client.getId())
				.balance(this.balance)
				.blocked(this.blocked)
				.currency(this.currency)
				.build();
	}

}
