package com.radakka.puff.events.game;

import java.util.Optional;

import com.radakka.puff.entity.game.Game;

import lombok.AllArgsConstructor;
import lombok.Data;
import reactor.core.publisher.FluxSink;

@Data
@AllArgsConstructor
public class EventsFluxData {
	
	FluxSink<Optional<Game>> sink;
	
	Long lastEventTimestamp;
	
}
