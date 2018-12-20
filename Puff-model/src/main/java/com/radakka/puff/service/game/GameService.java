package com.radakka.puff.service.game;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.radakka.puff.entity.game.Game;
import com.radakka.puff.repository.user.GameRepository;
import com.radakka.puff.utils.EntityIdUtils;
import com.radakka.puff.utils.GameUtils;

import reactor.core.publisher.Mono;

@Service
public class GameService {
	
	@Autowired
	private GameRepository gameRepository;
	
	public Mono<Game> createNewGame(List<String> userNames, int numberOfDecks) {
		Game game = GameUtils.initializeGame(userNames, numberOfDecks);
		
		return this.gameRepository.save(game);
	}
	
	public Mono<Game> retrieveGame(String username, String gameId) {
		return this.gameRepository.findById(EntityIdUtils.getGameId(gameId)).flatMap((game) -> {
			if(game.getPlayers().stream().filter((player) -> 
				player.getUsername().equals(username)).collect(Collectors.toList()).size() > 0) {
				return Mono.just(game);
			} else {
				return Mono.empty();
			}
		});
	}

}
