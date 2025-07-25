package com.github.dartzlight.revelio.outbound.helpers;

import com.github.dartzlight.revelio.domain.exceptions.UnknownGuildException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;

import java.util.Optional;

public class GuildSearcher {

	private GuildSearcher() {
	}

	public static Guild findGuild(JDA jda, String guildId) {
		return Optional.ofNullable(jda.getGuildById(guildId))
				.orElseThrow(() -> new UnknownGuildException(guildId));
	}
}
