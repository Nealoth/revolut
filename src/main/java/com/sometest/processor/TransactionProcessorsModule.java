package com.sometest.processor;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;
import com.sometest.model.TransactionType;

import static com.sometest.model.TransactionType.*;

public class TransactionProcessorsModule extends AbstractModule {

	@Override
	protected void configure() {
		MapBinder<TransactionType, TransactionProcessor> bindings =
				MapBinder.newMapBinder(binder(), TransactionType.class, TransactionProcessor.class);

		bindings.addBinding(INCOMING).to(IncomingTransactionProcessor.class);
		bindings.addBinding(OUTGOING).to(OutgoingTransactionProcessor.class);
		bindings.addBinding(BETWEEEN_ACCOUNTS).to(TransferTransactionProcessor.class);
	}

}
