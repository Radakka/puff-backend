package com.radakka.puff.entity.game;

import java.util.List;

import org.springframework.data.couchbase.core.mapping.Document;

import com.couchbase.client.java.repository.annotation.Field;
import com.radakka.puff.entity.AbstractEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Document
@Data
@EqualsAndHashCode(callSuper=true)	
public class Game extends AbstractEntity {
	
	@Field
	private List<Card> deck;
	
	@Field
	private List<Card> playedStack;
	
	@Field
	private List<Player> players;

}
