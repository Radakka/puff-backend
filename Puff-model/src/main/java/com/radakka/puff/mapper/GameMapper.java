package com.radakka.puff.mapper;

import java.util.ArrayList;
import java.util.List;

import org.mapstruct.Mapper;

import com.radakka.puff.dto.game.CardDTO;
import com.radakka.puff.dto.game.GameDTO;
import com.radakka.puff.dto.game.OponentDTO;
import com.radakka.puff.dto.game.PlayerDTO;
import com.radakka.puff.entity.game.Card;
import com.radakka.puff.entity.game.Game;
import com.radakka.puff.entity.game.Player;
import com.radakka.puff.rules.GameRules;

@Mapper
public interface GameMapper {

	CardDTO cardToDTO(Card card);

	List<CardDTO> cardsToDTO(List<Card> cards);

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
