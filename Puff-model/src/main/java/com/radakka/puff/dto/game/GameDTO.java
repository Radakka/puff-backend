package com.radakka.puff.dto.game;

import java.util.List;

import lombok.Data;

@Data
public class GameDTO {
	
	private Integer deckSize;
	
	private List<CardDTO> playedCards;
	
	private PlayerDTO player;
	
	private List<OponentDTO> oponents;
	
	private String currentTurn;
	
	private String gameId;

}
