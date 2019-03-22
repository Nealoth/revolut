package com.sometest.dto;

import com.sometest.model.TransactionStatus;
import com.sometest.model.TransactionType;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Builder
public class TransactionDTO {

	private Long id;

	private Long from;

	private Long to;

	@NotNull
	private BigDecimal amount;

	private TransactionType type;

	private TransactionStatus status;

}
