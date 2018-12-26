package com.radakka.puff.game.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.radakka.puff.dto.game.CardPlayDTO;
import com.radakka.puff.dto.game.GameDTO;
import com.radakka.puff.dto.game.NewGameRequestDTO;
import com.radakka.puff.mapper.GameMapper;
import com.radakka.puff.service.game.GameService;

import reactor.core.publisher.Mono;

@RestController
public class GameController {
	
	@Autowired
	private GameService gameService;
	
	@Autowired
	private GameMapper gameMapper;
	
	@PostMapping("/game/new")
	@PreAuthorize("hasRole('USER')")
	public Mono<GameDTO> startNewGame(@AuthenticationPrincipal String username, @RequestBody @Valid NewGameRequestDTO request) {
		return this.gameService.createNewGame(request.getUserNames(), request.getNumberOfDecks()).map((game) -> {
			return this.gameMapper.gameToDTO(game, username);
		});
	}
	
	@GetMapping("/game/{gameId}")
	@PreAuthorize("hasRole('USER')")
	public Mono<ResponseEntity<GameDTO>> retrieveGame(@AuthenticationPrincipal String username, @PathVariable String gameId) {
		return this.gameService.retrieveGame(username, gameId).map((game) -> {
			return ResponseEntity.ok(this.gameMapper.gameToDTO(game, username));
		}).defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}
	
	@PostMapping("/game/{gameId}/play")
	@PreAuthorize("hasRole('USER')")
	public Mono<ResponseEntity<GameDTO>> playCard(@AuthenticationPrincipal String username, @RequestBody @Valid CardPlayDTO cardPlay) {
		//TODO implement
		return Mono.just(ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build());
	}

}
