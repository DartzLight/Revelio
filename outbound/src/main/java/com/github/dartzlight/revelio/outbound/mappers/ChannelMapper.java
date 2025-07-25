package com.github.dartzlight.revelio.outbound.mappers;

import com.github.dartzlight.revelio.domain.model.Channel;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;

public class ChannelMapper {

	private ChannelMapper() {
	}

	public static Channel toDomain(MessageChannelUnion channel) {
		return new Channel(channel.getName());
	}

}
