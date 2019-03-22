package com.sometest.endpoint;

import com.sometest.dto.ClientDTO;
import com.sometest.service.ClientService;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import javax.inject.Singleton;

import static spark.Spark.*;

@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class ClientEndpoint extends AbstractEndpoint {

	private final ClientService clientService;
	private final String PREFIX = "/clients";

	@Override
	public void initPaths() {
		path(API_VERSION + PREFIX, () -> {
			get("/", (req, res) -> clientService.getClients(), gson::toJson);

			post("/", (req, res) -> {
				ClientDTO clientDTO = gson.fromJson(req.body(), ClientDTO.class);
				return clientService.createClient(clientDTO);
			}, gson::toJson);

			get("/:id", (req, res) -> clientService.getById(Long.valueOf(req.params(":id"))), gson::toJson);

			delete("/:id", (req, res) -> clientService.delete(Long.valueOf(req.params(":id"))), gson::toJson);

			get("/:id/accounts/", (req, res) -> clientService.getClientAcocunts(Long.valueOf(req.params(":id"))), gson::toJson);

		});
	}
}
