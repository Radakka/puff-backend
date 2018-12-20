package com.radakka.puff.dto.game;

import java.util.List;

import lombok.Data;

@Data
public class OponentDTO {
	
	private String username;

	private Integer hand;
	
	private List<CardDTO> faceUp;
	
	private Integer faceDown;
	
}
