package com.radakka.puff.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.radakka.puff.entity.game.Card;
import com.radakka.puff.entity.game.Game;
import com.radakka.puff.entity.game.Player;
import com.radakka.puff.entity.game.Suit;

public class GameUtils {
	
	public static Game initializeGame(List<String> userNames, int numberOfDecks) {
		Game game = new Game();
		game.setId(EntityIdUtils.generateNewGameId());
		game.setPlayedStack(new ArrayList<>());
		
		List<Card> deck = generateShuffledDeck(numberOfDecks);
		List<Player> players = new ArrayList<>();
		
		//TODO randomize turn order and starting player
		for(String user : userNames) {
			Player player = new Player();
			player.setUsername(user);
			
			List<Card> faceDown = new ArrayList<>();
			List<Card> faceUp = new ArrayList<>();
			List<Card> hand = new ArrayList<>();
			
			for(int i = 0; i < 3;i++) {
				faceDown.add(deck.remove(0));
				faceUp.add(deck.remove(0));
				hand.add(deck.remove(0));
			}
			
			player.setFaceDown(faceDown);
			player.setFaceUp(faceUp);
			player.setHand(hand);
			
			players.add(player);
		}
		
		game.setPlayers(players);
		game.setDeck(deck);
		
		return game;
	}

	private static List<Card> generateShuffledDeck(int numberOfDecks) {
		Random random = new Random();
		List<Card> shuffledDeck = new ArrayList<>();
		List<Card> deck = createDeck(numberOfDecks);
		while(deck.size()>0) {
			int randomPosition = random.nextInt(deck.size());
			shuffledDeck.add(deck.remove(randomPosition));
		}

		return shuffledDeck;
	}

	private static List<Card> createDeck(int numberOfDecks) {
		List<Card> deck = new ArrayList<>();
		
		for(int i = 0;i < numberOfDecks;i++) {
			for(Suit suit : Suit.values()) {
				for(int j = 1;j <= 10;j++) {
					deck.add(Card.builder().suit(suit).number(j).build());
				}
			}
		}

		return deck;
	}

}
