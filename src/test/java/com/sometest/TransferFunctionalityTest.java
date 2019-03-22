package com.sometest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Map;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static java.math.BigDecimal.ROUND_HALF_EVEN;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TransferFunctionalityTest {

	private final static int PORT = 8080;

	@BeforeClass
	public static void startApp() {
		Application application = new Application(PORT);
		application.start();
	}

	@Test
	public void test() {

		JsonObject firstClientRequest = new JsonObject();
		firstClientRequest.addProperty("firstName", "John");
		firstClientRequest.addProperty("lastName", "Doe");

		// Create first client entity
		createClient(firstClientRequest);

		JsonObject secondClientRequest = new JsonObject();
		secondClientRequest.addProperty("firstName", "Jane");
		secondClientRequest.addProperty("lastName", "Doe");

		// Create second client entity
		createClient(secondClientRequest);

		JsonObject firstClientBankAccountRequest = new JsonObject();
		firstClientBankAccountRequest.addProperty("clientId", "1");

		// Create bank account for first client
		createBankAccount(firstClientBankAccountRequest);

		JsonObject secondClientBankAccountRequest = new JsonObject();
		secondClientBankAccountRequest.addProperty("clientId", "2");

		// Create bank account for second client client
		createBankAccount(secondClientBankAccountRequest);

		JsonObject addFundsToFirstClientAccountRequest = new JsonObject();
		addFundsToFirstClientAccountRequest.addProperty("to", "1");
		addFundsToFirstClientAccountRequest.addProperty("amount", "10000.00");

		// Adding funds to first client account
		addFundsOnBankAccount(addFundsToFirstClientAccountRequest);

		// Checking that first client bank account has received funds
		assertThat(getClientBalance(1L), is(new BigDecimal("10000.00")));

		// Checking that second client bank account is empty
		assertThat(getClientBalance(2L), is(new BigDecimal("0.00")));

		JsonObject moneyTransferRequest = new JsonObject();
		moneyTransferRequest.addProperty("from", "1");
		moneyTransferRequest.addProperty("to", "2");
		moneyTransferRequest.addProperty("amount", "3000.00");
		makeTransfer(moneyTransferRequest);

		// Checking that funds was transferred
		assertThat(getClientBalance(1L), is(new BigDecimal("7000.00")));
		assertThat(getClientBalance(2L), is(new BigDecimal("3000.00")));

	}

	private void createClient(JsonObject jsonObject) {
		given()
				.body(jsonObject)
				.when()
				.post("/v1/clients/")
				.then()
				.statusCode(200);
	}

	private void createBankAccount(JsonObject jsonObject) {
		given()
				.body(jsonObject)
				.when()
				.post("/v1/accounts/")
				.then()
				.statusCode(200);
	}

	private void addFundsOnBankAccount(JsonObject jsonObject) {
		given()
				.body(jsonObject)
				.when()
				.post("/v1/transactions/income")
				.then()
				.statusCode(200);
	}

	private BigDecimal getClientBalance(Long clientId) {
		Float result = (Float) get("/v1/clients/{id}/accounts/", clientId)
				.then()
				.statusCode(200)
				.extract()
				.jsonPath()
				.getList("$", Map.class)
				.get(0)
				.get("balance");

		return BigDecimal
				.valueOf(result)
				.setScale(2, ROUND_HALF_EVEN);
	}

	private void makeTransfer(JsonObject jsonObject) {
		given()
				.body(jsonObject)
				.when()
				.post("/v1/transactions/transfer")
				.then()
				.statusCode(200);
	}

}