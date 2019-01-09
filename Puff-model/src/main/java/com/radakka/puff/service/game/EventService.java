package com.radakka.puff.service.game;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.radakka.puff.entity.game.Game;

import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.FluxSink;

@Service
public class EventService {
	
	private Map<String,	Map<String, FluxSink<Game>>> sinksByGame = new ConcurrentHashMap<>();
	
	public DirectProcessor<Game> registerProcessor(String gameId, String username) {
		DirectProcessor<Game> processor = DirectProcessor.create();
		FluxSink<Game> sink = processor.sink();
		
		Map<String, FluxSink<Game>> sinksByUser = this.sinksByGame.get(gameId);
		if(sinksByUser == null) {
			sinksByUser = new ConcurrentHashMap<>();
			this.sinksByGame.put(gameId, sinksByUser);
		}
		
		sinksByUser.put(username, sink);
		
		return processor;
	}
	
	public void publishEvent(String gameId, String username, Game game) {
		Map<String, FluxSink<Game>> sinksByUser = this.sinksByGame.get(gameId);
		if(sinksByUser != null) {
			for(Entry<String, FluxSink<Game>> sink : sinksByUser.entrySet()) {
				if(!sink.getKey().equals(username)) {
					sink.getValue().next(game);
				}
			}
		}
	}
	
	

}