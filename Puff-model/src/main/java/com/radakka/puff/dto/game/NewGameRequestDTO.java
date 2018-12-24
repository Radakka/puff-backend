package com.radakka.puff.dto.game;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class NewGameRequestDTO {

	@NotNull(message="Usernames are required")
	@NotEmpty(message="Usernames should not be less than 2")
	@Size(min=2, message="Usernames should not be less than 2")
	private List<String> userNames;
	
	@NotNull(message="Number of decks is required")
	@Min(value = 1, message="Number of decks should not be less than 1")
	private Integer numberOfDecks;
	
}
