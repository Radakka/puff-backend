package com.radakka.puff.service.game;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.radakka.puff.entity.game.Game;
import com.radakka.puff.events.game.EventsFluxData;

import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.FluxSink;

@Service
public class EventService {
	
	private Map<String,	Map<String, EventsFluxData>> sinksByGame = new ConcurrentHashMap<>();
	
	Logger logger = LoggerFactory.getLogger(EventService.class);
	
	public DirectProcessor<Optional<Game>> registerProcessor(String gameId, String username) {
		DirectProcessor<Optional<Game>> processor = DirectProcessor.create();
		FluxSink<Optional<Game>> sink = processor.sink();
		
		Map<String, EventsFluxData> sinksByUser = this.sinksByGame.get(gameId);
		if(sinksByUser == null) {
			sinksByUser = new ConcurrentHashMap<>();
			this.sinksByGame.put(gameId, sinksByUser);
		}
		
		sinksByUser.put(username, new EventsFluxData(sink, System.currentTimeMillis()));
		
		return processor;
	}
	
	public void publishEvent(String gameId, String username, Game game) {
		Map<String, EventsFluxData> sinksByUser = this.sinksByGame.get(gameId);
		if(sinksByUser != null) {
			for(Entry<String, EventsFluxData> eventFlux : sinksByUser.entrySet()) {
				if(!eventFlux.getKey().equals(username)) {
					eventFlux.getValue().setLastEventTimestamp(System.currentTimeMillis());
					eventFlux.getValue().getSink().next(Optional.of(game));
				}
			}
		}
	}
	
	//TODO parametrize times
	@Scheduled(fixedDelay=15000)
	public void sendKeepAlive() {
		for(Map<String, EventsFluxData> eventsFluxByUser : this.sinksByGame.values()) {
			for(EventsFluxData eventFlux : eventsFluxByUser.values()) {
				if(System.currentTimeMillis() - eventFlux.getLastEventTimestamp() > 30000) {
					eventFlux.setLastEventTimestamp(System.currentTimeMillis());
					eventFlux.getSink().next(Optional.empty());
				}
			}
		}
	}
	

}