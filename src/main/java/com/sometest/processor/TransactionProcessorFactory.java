package com.sometest.processor;

import com.google.common.base.Preconditions;
import com.sometest.model.TransactionType;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;
import java.util.Optional;

@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class TransactionProcessorFactory {

	private final Map<TransactionType, TransactionProcessor> processors;

	public TransactionProcessor getInstance(TransactionType type) {
		Preconditions.checkNotNull(type);
		TransactionProcessor transactionProcessor = processors.get(type);

		return Optional
				.ofNullable(transactionProcessor)
				.orElseThrow(() -> new RuntimeException("Unsupported type of transaction: " + type));
	}

}
