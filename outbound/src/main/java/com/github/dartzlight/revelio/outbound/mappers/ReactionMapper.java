package com.github.dartzlight.revelio.outbound.mappers;

import com.github.dartzlight.revelio.domain.model.Reaction;
import net.dv8tion.jda.api.entities.MessageReaction;

import java.util.List;

public class ReactionMapper {
	private ReactionMapper() {
	}

	public static List<Reaction> toDomain(List<MessageReaction> reactions) {
		return reactions.stream()
				.map(ReactionMapper::toDomain)
				.toList();
	}

	public static Reaction toDomain(MessageReaction reaction) {
		return new Reaction(reaction.getEmoji().getFormatted(), reaction.getCount());
	}

}
