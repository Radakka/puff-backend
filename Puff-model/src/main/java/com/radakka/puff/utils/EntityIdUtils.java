package com.radakka.puff.utils;

import java.util.UUID;

public class EntityIdUtils {
	
	public static String getUserId(String username) {
		return "PUFF::USER::"+username;
	}
	
	public static String getGameId(String uniqueId) {
		return "PUFF::GAME::"+uniqueId;
	}
	
	public static String generateNewGameId() {
		return getGameId(UUID.randomUUID().toString());
	}

}
