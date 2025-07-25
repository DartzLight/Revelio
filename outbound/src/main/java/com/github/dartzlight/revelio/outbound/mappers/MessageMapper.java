package com.github.dartzlight.revelio.outbound.mappers;

import com.github.dartzlight.revelio.domain.model.Author;

import java.util.Optional;

public class MessageMapper {
	
	private MessageMapper() {
	}

	public static com.github.dartzlight.revelio.domain.model.Message toDomain(net.dv8tion.jda.api.entities.Message message) {
		return new com.github.dartzlight.revelio.domain.model.Message(
				mapAuthor(message),
				message.getContentDisplay(),
				message.getTimeCreated().toInstant(),
				ReactionMapper.toDomain(message.getReactions()),
				ChannelMapper.toDomain(message.getChannel()),
				message.getJumpUrl()
		);
	}

	private static Author mapAuthor(net.dv8tion.jda.api.entities.Message message) {
		return Optional.ofNullable(message.getMember())
				.map(AuthorMapper::toDomain)
				.orElseGet(() -> AuthorMapper.toDomain(message.getAuthor()));
	}

}
