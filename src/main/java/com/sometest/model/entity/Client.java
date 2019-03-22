package com.sometest.model.entity;

import com.sometest.dto.ClientDTO;
import com.sometest.dto.Convertable;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Client implements Convertable<ClientDTO> {

	private Long id;
	private String firstName;
	private String lastName;
	private List<BankAccount> bankAccounts = new ArrayList<>();

	public Client(ClientDTO dto) {
		this.firstName = dto.getFirstName();
		this.lastName = dto.getLastName();
	}

	@Override
	public ClientDTO toDTO() {
		return ClientDTO.builder()
				.id(this.id)
				.firstName(this.firstName)
				.lastName(this.lastName)
				.build();
	}

}
