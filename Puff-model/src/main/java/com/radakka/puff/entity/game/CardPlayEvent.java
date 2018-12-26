package com.radakka.puff.entity.game;

import javax.validation.constraints.NotNull;

import com.couchbase.client.deps.com.fasterxml.jackson.annotation.JsonIgnore;
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
	@Field
	private Card playedCard;
	
	@Field
	private Boolean playAllSameCards;
	
	@JsonIgnore
	private Integer cardPosition;
	
	@JsonIgnore
	private boolean cardPlayed;
	
	@Field
	private String targetPlayer;
}
