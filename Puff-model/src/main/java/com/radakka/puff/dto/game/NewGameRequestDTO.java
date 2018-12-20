package com.radakka.puff.dto.game;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class NewGameRequestDTO {

	@NotNull
	@NotEmpty
	@Size(min=2)
	private List<String> userNames;
	
	@NotNull
	private Integer numberOfDecks;
	
}
