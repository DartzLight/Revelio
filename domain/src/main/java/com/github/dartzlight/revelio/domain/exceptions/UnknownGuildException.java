package com.github.dartzlight.revelio.domain.exceptions;

public class UnknownGuildException extends RuntimeException {
	public UnknownGuildException(String guildId) {
		super("Unknown guild with id: " + guildId);
	}
}
