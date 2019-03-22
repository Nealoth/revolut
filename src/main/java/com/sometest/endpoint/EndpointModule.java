package com.sometest.endpoint;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import lombok.extern.slf4j.Slf4j;

import static com.google.inject.matcher.Matchers.any;

@Slf4j
public class EndpointModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ClientEndpoint.class).asEagerSingleton();
		bind(BankAccountEndpoint.class).asEagerSingleton();
		bind(TransactionEndpoint.class).asEagerSingleton();

		bindListener(any(),
				new TypeListener() {
					@Override
					public <T> void hear(TypeLiteral<T> type, TypeEncounter<T> encounter) {
						encounter.register(
								(InjectionListener<T>) injectee -> {
									if (injectee instanceof AbstractEndpoint) {
										AbstractEndpoint abstractEndpoint = (AbstractEndpoint) injectee;
										abstractEndpoint.initPaths();
										String name = abstractEndpoint.getClass().getSimpleName();
										log.info("AbstractEndpoint[{}] successfully initialized", name);
									}
								});
					}
				});
	}

}
