package com.radakka.puff.entity.game;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.couchbase.client.java.repository.annotation.Field;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class StackCleanEvent extends GameEvent {
	@NotNull
	@NotEmpty
	@Field
	private List<Card> cardsCleaned;
}
