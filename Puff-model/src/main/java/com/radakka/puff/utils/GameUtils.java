package com.radakka.puff.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.radakka.puff.entity.game.Card;
import com.radakka.puff.entity.game.CardPlayEvent;
import com.radakka.puff.entity.game.Game;
import com.radakka.puff.entity.game.Player;
import com.radakka.puff.entity.game.Suit;
import com.radakka.puff.exception.GameRuleException;

public class GameUtils {

	public static Game initializeGame(List<String> userNames, int numberOfDecks) {

		List<String> users = new ArrayList<>(userNames.stream().distinct().collect(Collectors.toList()));

		//Validate that there is enough decks for the given players
		int cardsNeeded = users.size() * 9;
		if(cardsNeeded > numberOfDecks * 40) {
			throw new GameRuleException("Not enough cards in "+numberOfDecks+ " deck for "+userNames.size()+ " players");
		}

		Game game = new Game();
		game.setId(EntityIdUtils.generateNewGameId());
		game.setPlayedStack(new ArrayList<>());
		game.setEvents(new ArrayList<>());
		game.setCurrentTurn(1);
		game.setEnded(false);

		List<Card> deck = generateShuffledDeck(numberOfDecks);
		List<Player> players = new ArrayList<>();

		Random random = new Random();
		int turn = 1;
		while(users.size() > 0) {
			Player player = new Player();
			player.setUsername(users.remove(random.nextInt(users.size())));
			player.setWinner(false);
			player.setTurn(turn);
			turn++;

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

	public static CardPlayEvent addCardPlayedAndSequenceToEvent(String username, Player player, Game game, CardPlayEvent event) {
		try {
			if(game.getEnded()) {
				throw new GameRuleException("This game has ended");
			}
			event.setPlayedCards(new ArrayList<>());
			int playedNumber = 0;
			switch(event.getCardSource()) {
			case HAND:
				for(Integer position : event.getCardPosition()) {
					Card card = player.getHand().get(position);
					event.getPlayedCards().add(card);
					if(playedNumber == 0) {
						playedNumber = card.getNumber();
					} else if(playedNumber != card.getNumber()) {
						throw new GameRuleException("Multiple cards can only be played if they have the same number");
					}
				}
				break;
			case FACE_UP:
				for(Integer position : event.getCardPosition()) {
					Card card = player.getFaceUp().get(position);
					event.getPlayedCards().add(card);
					if(playedNumber == 0) {
						playedNumber = card.getNumber();
					} else if(playedNumber != card.getNumber()) {
						throw new GameRuleException("Multiple cards can only be played if they have the same number");
					}
				}
				break;
			case FACE_DOWN:
				if(event.getCardPosition().size() != 1) {
					throw new GameRuleException("Face down cards can only be played one by one");
				}
				event.getPlayedCards().add(player.getFaceDown().get(event.getCardPosition().get(0)));
				break;
			default:
				throw new GameRuleException("Invalid card source");
			}
		} catch(IndexOutOfBoundsException e) {
			throw new GameRuleException("Invalid card position");
		}

		if(game.getEvents().isEmpty()) {
			event.setEventSequence(0);
		} else {
			event.setEventSequence(game.getEvents().get(game.getEvents().size()-1).getEventSequence()+1);
		}

		return event;
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
