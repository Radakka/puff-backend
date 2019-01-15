package com.radakka.puff.dto;

import com.radakka.puff.dto.game.GameDTO;

import lombok.Data;

@Data
public class EventDTO {

	private GameDTO game;
	
	private Boolean keepAlive;
	
}
