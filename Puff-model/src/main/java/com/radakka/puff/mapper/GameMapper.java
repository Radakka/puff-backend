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

@Mapper
public interface GameMapper {
	
	CardDTO cardToDTO(Card card);
	
	List<CardDTO> cardsToDTO(List<Card> cards);
	
	default PlayerDTO playerToDTO(Player player) {
		PlayerDTO playerDTO = new PlayerDTO();
		
		playerDTO.setHand(this.cardsToDTO(player.getHand()));
		playerDTO.setFaceUp(this.cardsToDTO(player.getFaceUp()));
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
				gameDTO.setPlayer(this.playerToDTO(player));
			} else {
				gameDTO.getOponents().add(this.playerToOponentDTO(player));
			}
		}
		
		return gameDTO;
	}
}
