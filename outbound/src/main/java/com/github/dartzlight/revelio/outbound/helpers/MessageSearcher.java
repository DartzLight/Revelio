package com.github.dartzlight.revelio.outbound.helpers;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class MessageSearcher {

	private static final Logger LOGGER = LoggerFactory.getLogger(MessageSearcher.class);

	private MessageSearcher() {
	}

	public static List<Message> searchMessagesFromGuild(Guild guild, Instant oldestTimestampMessage, Instant latestTimestampMessage) {
		LOGGER.debug("Reading messages of guild {} from {} to {}", guild, oldestTimestampMessage, latestTimestampMessage);

		long skipId = TimeUtil.getDiscordTimestamp(latestTimestampMessage.toEpochMilli());
		List<Message> guildMessages = new ArrayList<>();
		for (var channel : guild.getTextChannels()) {
			if (guild.getSelfMember().hasPermission(channel, Permission.MESSAGE_HISTORY)) {
				LOGGER.debug("Reading channel: {}", channel);
				List<Message> channelMessages = channel.getIterableHistory()
						.skipTo(skipId)
						.takeWhileAsync(message -> message.getTimeCreated().toInstant().isAfter(oldestTimestampMessage))
						.join();
				guildMessages.addAll(channelMessages);
			} else {
				LOGGER.debug("Not allowed to read channel: {}", channel);
			}
		}

		return guildMessages.stream()
				.filter(message -> !message.getAuthor().isBot())
				.toList();
	}

}
