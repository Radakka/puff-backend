package com.radakka.puff.error;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.function.server.ServerRequest;

import com.radakka.puff.exception.UsersNotFoundException;

@Component
public class CustomErrorAttributes extends DefaultErrorAttributes {

	@Override
	public Map<String, Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace) {
		final Throwable t = getError(request);
		final Map<String, Object> errorAttributes = super.getErrorAttributes(request, includeStackTrace);

		final Map<String, Object> customErrorAttributes = new HashMap<>();

		customErrorAttributes.put("status", errorAttributes.get("status"));
		customErrorAttributes.put("error", errorAttributes.get("error"));
		
		List<String> errorMessages = null;
		if(t instanceof WebExchangeBindException) {
			errorMessages = extractDefaultMessage(errorAttributes);
		} else if(t instanceof UsersNotFoundException) {
			errorMessages = new ArrayList<>();
			for(String user : ((UsersNotFoundException) t).getUsersNotFound()) {
				errorMessages.add("Username "+user+" not found");
			}
		} else {
			errorMessages = new ArrayList<>();
			errorMessages.add(t.getMessage());
		}
		if(errorMessages != null && !errorMessages.isEmpty()) {
			customErrorAttributes.put("errorMessages", errorMessages);
		}

		return customErrorAttributes;
	}

	private List<String> extractDefaultMessage(Map<String, Object> errorAttributes) {
		List<String> errorMessages = new ArrayList<>();
		try {
			if(errorAttributes.get("errors") instanceof List) {
				List<Object> errors = (List<Object>) errorAttributes.get("errors");
				for(Object error : errors) {
					if(error instanceof FieldError) {
						FieldError fError = (FieldError) error;
						errorMessages.add(fError.getDefaultMessage());
					}
				}
			}

			return errorMessages;
		} catch(Exception e) {
			return errorMessages;
		}
	}

}
