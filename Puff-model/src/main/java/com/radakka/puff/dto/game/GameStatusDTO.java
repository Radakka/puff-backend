package com.radakka.puff.dto.game;

import lombok.Data;

@Data
public class GameStatusDTO {

	private String gameId;
	
	private Boolean gameEnded;
	
	private Boolean isUserTurn;
}
