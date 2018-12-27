package com.radakka.puff.rules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.radakka.puff.entity.game.Card;
import com.radakka.puff.entity.game.CardPlayEvent;
import com.radakka.puff.entity.game.CardSource;
import com.radakka.puff.entity.game.DrawEvent;
import com.radakka.puff.entity.game.Game;
import com.radakka.puff.entity.game.GameEvent;
import com.radakka.puff.entity.game.GameEventType;
import com.radakka.puff.entity.game.Player;
import com.radakka.puff.exception.GameRuleException;

public class GameRules {

	public static Boolean isPlayable(Card playedCard, List<Card> stack) {
		if(stack == null || stack.size() == 0) {
			return true;
		}

		Integer played = playedCard.getNumber();
		Integer topStack = stack.get(stack.size()-1).getNumber();
		switch(played) {
		case 8:
			if(topStack == 1) {
				return true;
			}
		case 9:
		case 10:
			if(topStack == 7) {
				return false;
			}
		case 3:
		case 4:
		case 5:
		case 6:
		case 7:
			if(topStack == 1) {
				return false;
			}
			if(played < topStack && topStack != 7) {
				return false;
			}
		}
		return true;
	}

	public static Game playCard(String username, Game game, CardPlayEvent event) {
		//Check if game has ended
		if(game.getEnded()) {
			throw new GameRuleException("This game has ended");
		}
		Player player = game.getPlayers().stream().filter((p) -> p.getUsername().equals(username)).collect(Collectors.toList()).get(0);
		//Validate turn
		if(player.getTurn() == game.getCurrentTurn()) {
			//Validate card source
			if(validateSource(event.getCardSource(), player)) {
				//Validate target player if the played card is a 1
				if(event.getPlayedCard().getNumber() == 1) {
					List<Player> targetPlayer = game.getPlayers().stream().filter(p -> p.getUsername().equals(event.getTargetPlayer())).collect(Collectors.toList());
					if(targetPlayer.isEmpty()) {
						throw new GameRuleException("Target player "+event.getTargetPlayer()+" is not in this game");
					}
				}
				//Validate played card
				if(isPlayable(event.getPlayedCard(), game.getPlayedStack())) {
					return doPlayCard(game, player, event);
				} else if(event.getCardSource().equals(CardSource.FACE_DOWN) || event.getCardSource().equals(CardSource.FACE_UP)) {
					return doDrawTableCard(game, player, event);
				} else {
					throw new GameRuleException("You can`t play that card");
				}
			} else {
				throw new GameRuleException("You can`t play cards from "+event.getCardSource());
			}
		} else {
			throw new GameRuleException("Not your turn");
		}
	}
	
	private static boolean checkWin(List<Player> players, CardPlayEvent event) {
		for(Player player : players) {
			if(player.getHand().isEmpty() && player.getFaceUp().isEmpty() && player.getFaceDown().isEmpty()) {
				if(event.getPlayedCard().getNumber() != 1 || !event.getTargetPlayer().equals(player.getUsername())) {
					player.setWinner(true);
					return true;
				}
			}
		}
		return false;
	}

	private static Game doPlayCard(Game game, Player player, CardPlayEvent event) {
		//TODO implement play all card with same number
		switch(event.getCardSource()) {
		case HAND:
			player.getHand().remove(event.getCardPosition().intValue());
			game.getPlayedStack().add(event.getPlayedCard());
			break;
		case FACE_UP:
			player.getFaceUp().remove(event.getCardPosition().intValue());
			game.getPlayedStack().add(event.getPlayedCard());
			break;
		case FACE_DOWN:
			player.getFaceDown().remove(event.getCardPosition().intValue());
			game.getPlayedStack().add(event.getPlayedCard());
			break;
		default:
			break;
		}
		game.getEvents().add(event);
		if(checkWin(game.getPlayers(), event)) {
			game.setEnded(true);
			return game;
		}
		
		return triggerNextTurn(game, player, event);
	}

	private static Game doDrawTableCard(Game game, Player player, CardPlayEvent event) {
		//TODO implement draw all cards with same number
		List<Card> cardSource = player.getFaceDown();
		if(event.getCardSource().equals(CardSource.FACE_UP)) {
			cardSource = player.getFaceUp();
		}
		cardSource.remove(event.getCardPosition().intValue());
		player.getHand().add(event.getPlayedCard());
		
		DrawEvent drawEvent = new DrawEvent();
		drawEvent.setEventType(GameEventType.CARD_DRAW);
		drawEvent.setCardSource(event.getCardSource());
		drawEvent.setEventSequence(event.getEventSequence() + 1);
		drawEvent.setPlayer(player.getUsername());
		drawEvent.setCardsDrawn(Arrays.asList(event.getPlayedCard()));
		
		return triggerNextTurn(game, player, event);
	}

	private static DrawEvent doDrawDeckCard(Game game, Player player, int eventSequence) {
		Card drawnCard = game.getDeck().remove(0);
		player.getHand().add(drawnCard);
		DrawEvent event = new DrawEvent();
		event.setEventType(GameEventType.CARD_DRAW);
		event.setCardSource(CardSource.DECK);
		event.setEventSequence(eventSequence);
		event.setPlayer(player.getUsername());
		event.setCardsDrawn(Arrays.asList(drawnCard));
		return event;
	}

	private static DrawEvent doDrawStack(Game game, Player player, int eventSequence) {
		List<Card> drawnCards = new ArrayList<>(game.getPlayedStack());
		player.getHand().addAll(drawnCards);
		DrawEvent event = new DrawEvent();
		event.setEventType(GameEventType.CARD_DRAW);
		event.setCardSource(CardSource.STACK);
		event.setEventSequence(eventSequence);
		event.setPlayer(player.getUsername());
		event.setCardsDrawn(drawnCards);
		return event;
	}

	private static Game triggerNextTurn(Game game, Player player, GameEvent event) {
		int eventSequence = event.getEventSequence();
		//Check if current player has to draw cards
		while(game.getDeck().size() > 0 && player.getHand().size() < 3) {
			game.getEvents().add(doDrawDeckCard(game, player, ++eventSequence));
		}

		//If the card played is a 9, the player must play another card. If not, is the next player turn
		Player nextPlayer = player;
		if(event instanceof DrawEvent || (event instanceof CardPlayEvent && ((CardPlayEvent) event).getPlayedCard().getNumber() != 9)) {
			//If a player plays a 1, the next turn is chosen by the player
			if(event instanceof CardPlayEvent && ((CardPlayEvent) event).getPlayedCard().getNumber() == 1) {
				nextPlayer = game.getPlayers().stream().filter(p -> p.getUsername().equals(((CardPlayEvent) event).getTargetPlayer())).collect(Collectors.toList()).get(0);
				game.setCurrentTurn(nextPlayer.getTurn());
			} else {
				int nextTurn = (game.getCurrentTurn() % game.getPlayers().size()) + 1;
				game.setCurrentTurn(nextTurn);
				nextPlayer = game.getPlayers().stream().filter(p -> p.getTurn() == nextTurn).collect(Collectors.toList()).get(0);
			}
		}

		if(checkPlayableCards(game, nextPlayer) == false) {
			GameEvent drawStackEvent = doDrawStack(game,nextPlayer, ++eventSequence);
			game.getEvents().add(drawStackEvent);
			return triggerNextTurn(game, nextPlayer, drawStackEvent);
		}
		return game;
	}

	private static boolean checkPlayableCards(Game game, Player player) {
		if(player.getHand().size() > 0) {
			for(Card card : player.getHand()) {
				if(isPlayable(card, game.getPlayedStack())) {
					return true;
				}
			}
		} else {
			//If a player has no cards in hand, he has to draw one from the table
			return true;
		}
		//If all the cards a player has in hand can`t be played, they had to draw all the cards in the stack
		return false;
	}

	private static boolean validateSource(CardSource source, Player player) {
		switch(source) {
		case HAND:
			return true;
		case FACE_UP:
			if(player.getHand().isEmpty()) {
				return true;
			}
			break;
		case FACE_DOWN:
			if(player.getHand().isEmpty() && player.getFaceUp().isEmpty()) {
				return true;
			}
			break;
		default:
			return false;
		}
		return false;
	}

}
