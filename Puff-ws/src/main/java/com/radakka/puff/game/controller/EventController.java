package com.radakka.puff.game.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.radakka.puff.dto.game.GameDTO;
import com.radakka.puff.entity.game.Game;
import com.radakka.puff.mapper.GameMapper;
import com.radakka.puff.service.game.EventService;

import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@RestController
public class EventController {
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	private GameMapper gameMapper;
	
	@GetMapping(path = "/game/{gameId}/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	@PreAuthorize("hasRole('USER')")
	public Flux<GameDTO> eventsFlux(@PathVariable String gameId, @AuthenticationPrincipal String username) {
		DirectProcessor<Game> processor = eventService.registerProcessor(gameId, username);
		return processor.subscribeOn(Schedulers.elastic()).map((game) -> {
			return gameMapper.gameToDTO(game, username);
		});
	}

}
