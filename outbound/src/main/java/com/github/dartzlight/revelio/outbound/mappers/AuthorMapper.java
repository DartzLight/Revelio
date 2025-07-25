package com.github.dartzlight.revelio.outbound.mappers;

import com.github.dartzlight.revelio.domain.model.Author;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

public class AuthorMapper {
	
	private AuthorMapper() {
	}

	public static Author toDomain(Member member) {
		return new Author(member.getEffectiveName(), member.getEffectiveAvatarUrl(), member.getColorRaw());
	}

	public static Author toDomain(User user) {
		return new Author(user.getEffectiveName(), user.getEffectiveAvatarUrl());
	}

}
