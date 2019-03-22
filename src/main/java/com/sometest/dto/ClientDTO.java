package com.sometest.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class ClientDTO {

	private long id;

	@NotNull
	private String firstName;

	@NotNull
	private String lastName;

}
