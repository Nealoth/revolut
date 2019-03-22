package com.sometest.dto;

import com.sometest.model.Currency;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Builder
public class BankAccountDTO {

	private Long id;

	@NotNull
	private Long clientId;

	private BigDecimal balance;

	private boolean blocked;

	private Currency currency;
}
