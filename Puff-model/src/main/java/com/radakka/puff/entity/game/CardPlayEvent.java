package com.radakka.puff.entity.game;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Transient;

import com.couchbase.client.java.repository.annotation.Field;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class CardPlayEvent extends GameEvent {

	@NotNull
	@Field
	private CardSource cardSource;
	
	@NotNull
	@NotEmpty
	@Field
	private List<Card> playedCards;
	
	@Transient
	private List<Integer> cardPosition;
	
	@Field
	private String targetPlayer;
}
