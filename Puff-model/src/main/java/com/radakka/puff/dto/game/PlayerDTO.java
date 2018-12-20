package com.radakka.puff.dto.game;

import java.util.List;

import lombok.Data;

@Data
public class PlayerDTO {

	private List<CardDTO> hand;
	
	private List<CardDTO> faceUp;
	
	private Integer faceDown;
	
	private String username;
	
}
