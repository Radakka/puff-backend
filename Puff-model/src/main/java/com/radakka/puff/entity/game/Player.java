package com.radakka.puff.entity.game;

import java.util.List;

import com.couchbase.client.java.repository.annotation.Field;

import lombok.Data;

@Data
public class Player {

	@Field
	private String username;
	
	@Field
	private List<Card> hand;
	
	@Field
	private List<Card> faceUp;
	
	@Field
	private List<Card> faceDown;
	
	@Field
	private int turn;
	
	
	
}
