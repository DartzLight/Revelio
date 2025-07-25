package com.github.dartzlight.revelio.domain.model;

public record Author(String name, String avatarUrl, int color) {
	public Author(String name, String avatarUrl) {
		this(name, avatarUrl, 0x999999);
	}
}
