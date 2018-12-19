package com.radakka.puff.entity.game;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Card {
	
	private Integer number;
	
	private Suit suit;

}
