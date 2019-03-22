package com.sometest;

import com.google.gson.Gson;
import com.sometest.exception.EntityNotFoundException;
import com.sometest.exception.TransactionFailException;
import com.sometest.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static spark.Spark.*;

@Slf4j
public abstract class SparkBootstrapper {

	private final int serverPort;
	private final Gson gson = new Gson();

	protected SparkBootstrapper(int serverPort) {
		this.serverPort = serverPort;
	}

	protected void configureSpark() {
		port(this.serverPort);
		log.info("Spark jetty listening port configured on " + this.serverPort);

		before(((request, response) -> {
			log.info("Catch {} request on {} with body: {}", request.requestMethod(), request.pathInfo(), request.body());
			response.type("application/json");
		}));

		after((request, response) -> {
			response.header("Content-Encoding", "gzip");
		});

		afterAfter(((request, response) -> {
			log.info("Server responded from {} {} with body: {}", request.requestMethod(), request.pathInfo(), response.body());
		}));

		exception(ValidationException.class, ((exception, request, response) -> {
			log.error("Validation error on path {} {}", request.requestMethod(), request.pathInfo());

			response.status(400);

			List<String> violations = exception
					.getViolations()
					.stream()
					.filter(Objects::nonNull)
					.map(p -> p.getPropertyPath().toString() + ": " + p.getMessage())
					.collect(Collectors.toList());

			String responseBody = violations.size() > 0 ? gson.toJson(violations) : exception.getMessage();
			response.body(responseBody);
		}));

		exception(EntityNotFoundException.class, ((exception, request, response) -> {
			log.error("Entity not found error on path {} {}", request.requestMethod(), request.pathInfo());

			response.status(404);
			response.body(exception.getMessage());
		}));

		exception(TransactionFailException.class, ((exception, request, response) -> {
			log.error("Transaction fails on {} {} with message: {}", request.requestMethod(), request.pathInfo(), exception.getMessage());

			response.status(500);
			response.body(exception.getMessage());
		}));
	}

}
