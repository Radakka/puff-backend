package com.radakka.puff.rules;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.radakka.puff.entity.game.Card;
import com.radakka.puff.entity.game.CardPlayEvent;
import com.radakka.puff.entity.game.CardSource;
import com.radakka.puff.entity.game.DrawEvent;
import com.radakka.puff.entity.game.Game;
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
		Player player = game.getPlayers().stream().filter((p) -> p.getUsername().equals(username)).collect(Collectors.toList()).get(0);
		//Validate turn
		if(player.getTurn() == game.getCurrentTurn()) {
			//Validate card source
			if(validateSource(event.getCardSource(), player)) {
				//Validate played card
				if(isPlayable(event.getPlayedCard(), game.getPlayedStack())) {
					return doPlayCard(game, player, event);
				} else if(event.getCardSource().equals(CardSource.FACE_DOWN)) {
					return doDrawFaceDownCard(game, player, event);
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
		return triggerNextTurn(game, player, event);
	}
	
	private static Game doDrawFaceDownCard(Game game, Player player, CardPlayEvent event) {
		player.getFaceDown().remove(event.getCardPosition().intValue());
		player.getHand().add(event.getPlayedCard());
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
	
	private static Game triggerNextTurn(Game game, Player player, CardPlayEvent event) {
		int eventSequence = event.getEventSequence();
		//Check if current player has to draw cards
		while(game.getDeck().size() > 0 && player.getHand().size() < 3) {
			game.getEvents().add(doDrawDeckCard(game, player, ++eventSequence));
		}
		//TODO check next player turn
		return game;
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
