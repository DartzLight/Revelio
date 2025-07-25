package com.github.dartzlight.revelio.domain.ports;

import com.github.dartzlight.revelio.domain.model.Message;

import java.time.Instant;
import java.util.List;

public interface MessageOutboundPort {

	List<Message> retrieveAllMessages(String guildId, Instant oldestMessage, Instant latestMessage);

}
