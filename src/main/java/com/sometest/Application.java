package com.sometest;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.sometest.endpoint.EndpointModule;
import com.sometest.processor.TransactionProcessorsModule;
import com.sometest.repository.impl.RepositoryModule;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import spark.Spark;

@Getter
@Slf4j
public class Application extends SparkBootstrapper {

	private final Injector injector;

	public Application(int serverPort) {
		super(serverPort);
		configureSpark();

		this.injector = createDefaultInjector();
	}

	public Application(Injector injector, int serverPort) {
		super(serverPort);
		configureSpark();

		this.injector = injector;
	}

	private static Injector createDefaultInjector() {
		return Guice
				.createInjector(
						new EndpointModule(),
						new RepositoryModule(),
						new TransactionProcessorsModule()
				);
	}

	public void start() {
		Spark.init();
	}
}
