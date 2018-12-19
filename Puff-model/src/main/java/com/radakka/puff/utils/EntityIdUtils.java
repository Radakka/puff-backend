package com.radakka.puff.utils;

import java.util.UUID;

public class EntityIdUtils {
	
	public static String getUserId(String username) {
		return "PUFF::USER::"+username;
	}
	
	public static String getGameId(UUID uniqueId) {
		return "PUFF::GAME::"+uniqueId.toString();
	}
	
	public static String generateNewGameId() {
		return getGameId(UUID.randomUUID());
	}

}
