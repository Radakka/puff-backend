package com.radakka.puff.game.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.radakka.puff.dto.game.NewGameRequest;
import com.radakka.puff.entity.game.Game;
import com.radakka.puff.service.game.GameService;

import reactor.core.publisher.Mono;

@RestController
public class GameController {
	
	@Autowired
	private GameService gameService;
	
	@PostMapping("/game/new")
	@PreAuthorize("hasRole('USER')")
	public Mono<Game> startNewGame(@AuthenticationPrincipal String username, @RequestBody NewGameRequest request) {
		//TODO Change return type to sanitized DTO
		//TODO Validate input (users, number of decks)
		return this.gameService.createNewGame(request.getUserNames(), request.getNumberOfDecks());
	}

}
