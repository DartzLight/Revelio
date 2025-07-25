package com.github.dartzlight.revelio.outbound.adapters;

import com.github.dartzlight.revelio.domain.model.Message;
import com.github.dartzlight.revelio.domain.ports.MessageOutboundPort;
import com.github.dartzlight.revelio.outbound.helpers.GuildSearcher;
import com.github.dartzlight.revelio.outbound.helpers.MessageSearcher;
import com.github.dartzlight.revelio.outbound.mappers.MessageMapper;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;

import java.time.Instant;
import java.util.List;

public class MessageAdapter implements MessageOutboundPort {

	private final JDA jda;

	public MessageAdapter(JDA jda) {
		this.jda = jda;
	}

	@Override
	public List<Message> retrieveAllMessages(String guildId, Instant oldestMessage, Instant latestMessage) {
		Guild guild = GuildSearcher.findGuild(jda, guildId);
		return MessageSearcher.searchMessagesFromGuild(guild, oldestMessage, latestMessage)
				.stream()
				.map(MessageMapper::toDomain)
				.toList();
	}

}
