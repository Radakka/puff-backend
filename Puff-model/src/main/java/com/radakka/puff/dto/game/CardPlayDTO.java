package com.radakka.puff.dto.game;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.radakka.puff.entity.game.CardSource;

import lombok.Data;

@Data
public class CardPlayDTO {

	@NotNull
	private CardSource cardSource;
	
	@NotNull
	@NotEmpty
	private List<Integer> cardPosition;
	
	private String targetPlayer;
}
