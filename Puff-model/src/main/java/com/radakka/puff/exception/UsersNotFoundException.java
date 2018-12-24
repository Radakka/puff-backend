package com.radakka.puff.exception;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class UsersNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -836141900318627208L;
	
	private final List<String> usersNotFound;
	
}
