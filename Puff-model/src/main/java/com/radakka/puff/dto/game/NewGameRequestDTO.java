package com.radakka.puff.dto.game;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class NewGameRequestDTO {

	@NotNull
	@NotEmpty
	private List<String> userNames;
	
	@NotNull
	private Integer numberOfDecks;
	
}
