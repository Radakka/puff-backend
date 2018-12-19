package com.radakka.puff.repository.user;

import org.springframework.data.couchbase.repository.ReactiveCouchbaseRepository;

import com.radakka.puff.entity.game.Game;

public interface GameRepository extends ReactiveCouchbaseRepository<Game, String> {

}
