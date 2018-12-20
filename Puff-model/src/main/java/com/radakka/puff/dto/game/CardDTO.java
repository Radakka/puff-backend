package com.radakka.puff.dto.game;

import com.radakka.puff.entity.game.Suit;

import lombok.Data;

@Data
public class CardDTO {

	private Integer number;
	
	private Suit suit;
	
}
