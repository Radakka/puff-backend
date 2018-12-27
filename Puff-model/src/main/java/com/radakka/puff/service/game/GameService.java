package com.radakka.puff.service.game;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.radakka.puff.entity.game.CardPlayEvent;
import com.radakka.puff.entity.game.Game;
import com.radakka.puff.exception.UsersNotFoundException;
import com.radakka.puff.repository.user.GameRepository;
import com.radakka.puff.repository.user.UserRepository;
import com.radakka.puff.rules.GameRules;
import com.radakka.puff.utils.EntityIdUtils;
import com.radakka.puff.utils.GameUtils;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class GameService {
	
	@Autowired
	private GameRepository gameRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	public Mono<Game> createNewGame(List<String> userNames, int numberOfDecks) {
		
		return this.getMissingUsers(new ArrayList<>(userNames)).collect(Collectors.toList()).flatMap((usersNotFound) -> {
			if(usersNotFound.isEmpty()) {
				Game game = GameUtils.initializeGame(userNames, numberOfDecks);
				
				return this.gameRepository.save(game).doOnSuccess((createdGame) -> {
					this.userRepository.findAllById(Flux.fromIterable(userNames).flatMap((username) -> {
						return Mono.just(EntityIdUtils.getUserId(username));
					})).subscribe((user) -> {
						if(user.getGames() == null) {
							user.setGames(new ArrayList<>());
						}
						user.getGames().add(EntityIdUtils.extractGameId(createdGame.getId()));
						this.userRepository.save(user).subscribe();
					});
				});
			} else {
				throw new UsersNotFoundException(usersNotFound);
			}
		});
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
	
	public Mono<Game> playCard(String username, String gameId, CardPlayEvent cardPlayed) {
		return this.gameRepository.findById(EntityIdUtils.getGameId(gameId)).flatMap((game) -> {
			if(game.getPlayers().stream().filter((player) -> 
				player.getUsername().equals(username)).collect(Collectors.toList()).size() > 0) {
				return Mono.just(GameRules.playCard(username, game, cardPlayed));
			} else {
				return Mono.empty();
			}
		});
	}
	
	public Flux<Game> retrieveAllGames(String username) {
		return this.gameRepository.findAllById(this.userRepository.findById(EntityIdUtils.getUserId(username)).flatMapIterable(user -> {
			return user.getGames();
		}).map((gameId) -> {
			return EntityIdUtils.getGameId(gameId);
		}));
	}
	
	private Flux<String> getMissingUsers(List<String> usernames) {
		return Flux.fromIterable(usernames).distinct().flatMap((username) -> {
			return this.userRepository.existsById(EntityIdUtils.getUserId(username)).flatMap((exists) -> {
				if(exists) {
					return Mono.empty();
				} else {
					return Mono.just(username);
				}
			});
		});
	}

}
