package com.radakka.puff.entity.game;

import javax.validation.constraints.NotNull;

import com.couchbase.client.deps.com.fasterxml.jackson.annotation.JsonSubTypes;
import com.couchbase.client.deps.com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.couchbase.client.deps.com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.couchbase.client.java.repository.annotation.Field;

import lombok.Data;

@Data
@JsonTypeInfo(
		use = JsonTypeInfo.Id.NAME, 
		include = As.PROPERTY, 
		property = "eventType")
@JsonSubTypes({
	@JsonSubTypes.Type(value = CardPlayEvent.class, name = "CARD_PLAY"),
	@JsonSubTypes.Type(value = DrawEvent.class, name = "CARD_DRAW"),
	@JsonSubTypes.Type(value = StackCleanEvent.class, name = "STACK_CLEAN")
})
public class GameEvent {

	@NotNull
	@Field
	private Integer eventSequence;

	@NotNull
	@Field
	private String player;

	@NotNull
	@Field
	private GameEventType eventType;

}
