package com.sometest.endpoint;

import com.sometest.dto.BankAccountDTO;
import com.sometest.service.BankAccountService;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import javax.inject.Singleton;

import static spark.Spark.*;

@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class BankAccountEndpoint extends AbstractEndpoint {

	private final BankAccountService bankAccountService;
	private final String PREFIX = "/accounts";

	@Override
	public void initPaths() {
		path(API_VERSION + PREFIX, () -> {
			post("/", (req, res) -> {
				BankAccountDTO bankAccountDTO = gson.fromJson(req.body(), BankAccountDTO.class);
				return bankAccountService.createBankAccount(bankAccountDTO);
			}, gson::toJson);

			get("/", (req, res) -> bankAccountService.getBankAccounts(), gson::toJson);
			get("/:id", (req, res) -> bankAccountService.getBankAccountById(Long.valueOf(req.params(":id"))), gson::toJson);
		});
	}

}
