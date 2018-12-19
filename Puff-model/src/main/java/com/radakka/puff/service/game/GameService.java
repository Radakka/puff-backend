package com.radakka.puff.service.game;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.radakka.puff.entity.game.Game;
import com.radakka.puff.repository.user.GameRepository;
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

}
