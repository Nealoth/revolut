package com.sometest.endpoint;

import com.sometest.dto.TransactionDTO;
import com.sometest.service.TransactionService;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import javax.inject.Singleton;

import static spark.Spark.path;
import static spark.Spark.post;

@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class TransactionEndpoint extends AbstractEndpoint {

	private final TransactionService transactionService;
	private final String PREFIX = "/transactions";

	@Override
	public void initPaths() {
		path(API_VERSION + PREFIX, () -> {
			post("/income", (req, res) -> {
				TransactionDTO transactionDTO = gson.fromJson(req.body(), TransactionDTO.class);
				transactionService.income(transactionDTO);
				return null;
			}, gson::toJson);

			post("/withdraw", (req, res) -> {
				TransactionDTO transactionDTO = gson.fromJson(req.body(), TransactionDTO.class);
				transactionService.withdraw(transactionDTO);
				return null;
			}, gson::toJson);

			post("/transfer", (req, res) -> {
				TransactionDTO transactionDTO = gson.fromJson(req.body(), TransactionDTO.class);
				transactionService.transfer(transactionDTO);
				return null;
			}, gson::toJson);
		});
	}
}
