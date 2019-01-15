package com.radakka.puff.game.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.radakka.puff.dto.EventDTO;
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
	public Flux<EventDTO> eventsFlux(@PathVariable String gameId, @AuthenticationPrincipal String username) {
		DirectProcessor<Optional<Game>> processor = eventService.registerProcessor(gameId, username);
		return processor.subscribeOn(Schedulers.elastic()).map((game) -> {
			EventDTO event = new EventDTO();
			if(game.isPresent()) {
				event.setGame(gameMapper.gameToDTO(game.get(), username));
				event.setKeepAlive(false);
			} else {
				event.setKeepAlive(true);
			}
			return event;
		});
	}

}
