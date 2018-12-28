package com.radakka.puff.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;

import com.radakka.puff.dto.game.CardDTO;
import com.radakka.puff.dto.game.CardPlayDTO;
import com.radakka.puff.dto.game.GameDTO;
import com.radakka.puff.dto.game.GameStatusDTO;
import com.radakka.puff.dto.game.OponentDTO;
import com.radakka.puff.dto.game.PlayerDTO;
import com.radakka.puff.entity.game.Card;
import com.radakka.puff.entity.game.CardPlayEvent;
import com.radakka.puff.entity.game.Game;
import com.radakka.puff.entity.game.GameEventType;
import com.radakka.puff.entity.game.Player;
import com.radakka.puff.rules.GameRules;
import com.radakka.puff.utils.EntityIdUtils;

@Mapper
public interface GameMapper {

	CardDTO cardToDTO(Card card);

	List<CardDTO> cardsToDTO(List<Card> cards);

	default CardPlayEvent cardPlayDTOToEvent(String username, CardPlayDTO playDTO) {
		CardPlayEvent event = new CardPlayEvent();

		event.setCardSource(playDTO.getCardSource());
		event.setEventType(GameEventType.CARD_PLAY);
		event.setPlayer(username);
		event.setTargetPlayer(playDTO.getTargetPlayer());
		event.setCardPosition(playDTO.getCardPosition());

		return event;
	}

	default GameStatusDTO gameToStatusDTO(Game game, String username) {
		GameStatusDTO gameStatus = new GameStatusDTO();
		gameStatus.setGameId(EntityIdUtils.extractGameId(game.getId()));
		gameStatus.setGameEnded(game.getEnded());

		Player currentPlayer = game.getPlayers().stream().filter(p -> p.getTurn() == game.getCurrentTurn()).collect(Collectors.toList()).get(0);
		if(username.equals(currentPlayer.getUsername())) {
			gameStatus.setIsUserTurn(true);
		} else {
			gameStatus.setIsUserTurn(false);
		}

		return gameStatus;
	}

	default List<CardDTO> cardsToDTOPlayable(List<Card> cards, List<Card> stack) {
		List<CardDTO> cardsDTO = new ArrayList<>();

		for(Card card : cards) {
			CardDTO cardDTO = this.cardToDTO(card);
			cardDTO.setPlayable(GameRules.isPlayable(card, stack));
			cardsDTO.add(cardDTO);
		}

		return cardsDTO;
	}

	default PlayerDTO playerToDTO(Player player, List<Card> stack) {
		PlayerDTO playerDTO = new PlayerDTO();

		playerDTO.setHand(this.cardsToDTOPlayable(player.getHand(), stack));
		if(player.getHand() == null || player.getHand().size() == 0) {
			playerDTO.setFaceUp(this.cardsToDTOPlayable(player.getFaceUp(), stack));
		} else {
			playerDTO.setFaceUp(this.cardsToDTO(player.getFaceUp()));
		}
		playerDTO.setFaceDown(player.getFaceDown().size());
		playerDTO.setUsername(player.getUsername());

		return playerDTO;
	}

	default OponentDTO playerToOponentDTO(Player player) {
		OponentDTO oponent = new OponentDTO();

		oponent.setUsername(player.getUsername());
		oponent.setFaceDown(player.getFaceDown().size());
		oponent.setHand(player.getHand().size());
		oponent.setFaceUp(this.cardsToDTO(player.getFaceUp()));

		return oponent;
	}

	default GameDTO gameToDTO(Game game, String user) {
		GameDTO gameDTO = new GameDTO();

		gameDTO.setDeckSize(game.getDeck().size());
		gameDTO.setPlayedCards(this.cardsToDTO(game.getPlayedStack()));
		gameDTO.setOponents(new ArrayList<>());
		gameDTO.setGameId(EntityIdUtils.extractGameId(game.getId()));

		for(Player player : game.getPlayers()) {
			if(game.getCurrentTurn() == player.getTurn()) {
				gameDTO.setCurrentTurn(player.getUsername());
			}

			if(user.equals(player.getUsername())) {
				gameDTO.setPlayer(this.playerToDTO(player, game.getPlayedStack()));

			} else {
				gameDTO.getOponents().add(this.playerToOponentDTO(player));
			}
		}

		return gameDTO;
	}
}
