package com.radakka.puff.dto.game;

import javax.validation.constraints.NotNull;

import com.radakka.puff.entity.game.CardSource;

import lombok.Data;

@Data
public class CardPlayDTO {

	@NotNull
	private CardSource cardSource;
	
	@NotNull
	private Integer cardPosition;
	
	private String targetPlayer;
	
	private Boolean playAllSameCards;
}
